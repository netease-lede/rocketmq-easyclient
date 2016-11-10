package com.netease.lottery.easymq.consumer.scanner;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.netease.lottery.easymq.common.constant.MQConstant;
import com.netease.lottery.easymq.common.exception.MqConsumerConfigException;
import com.netease.lottery.easymq.consumer.bean.MQConsumerConfigBean;
import com.netease.lottery.easymq.consumer.handler.MQRecMsgHandler;
import com.netease.lottery.easymq.consumer.handler.annotation.MQConsumerMeta;

public class ScanPackage
{
	private final static Log LOG = LogFactory.getLog(ScanPackage.class);
	//扫描  scanPackages 下的文件的匹配符
	protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	public static Set<String> findAnnotationClass(String scanPackages, Class<? extends Annotation> annotation)
	{
		//获取所有的类
		Set<String> fullClazzSet = findPackageClass(scanPackages);
		Set<String> clazzSet = Sets.newHashSet();
		for (String clazz : fullClazzSet)
		{
			try
			{
				if (findAnnotationClasses(clazz, annotation))
				{
					clazzSet.add(clazz);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return clazzSet;
	}

	/**
	 * 结合spring的类扫描方式
	 * 根据需要扫描的包路径及相应的注解，获取最终测method集合
	 * 仅返回public方法，如果方法是非public类型的，不会被返回
	 * 可以扫描工程下的class文件及jar中的class文件
	 *
	 * @param scanPackages
	 * @param annotation
	 * @return
	 */
	public static Set<Method> findClassAnnotationMethods(String scanPackages, Class<? extends Annotation> annotation)
	{
		//获取所有的类
		Set<String> clazzSet = findPackageClass(scanPackages);
		Set<Method> methods = new HashSet<Method>();
		//遍历类，查询相应的annotation方法
		for (String clazz : clazzSet)
		{
			try
			{
				Set<Method> ms = findAnnotationMethods(clazz, annotation);
				if (ms != null)
				{
					methods.addAll(ms);
				}
			}
			catch (ClassNotFoundException ignore)
			{
			}
		}
		return methods;
	}

	/**
	 * 根据扫描包的,查询下面的所有类
	 *
	 * @param scanPackages 扫描的package路径
	 * @return
	 */
	public static Set<String> findPackageClass(String scanPackages)
	{
		if (StringUtils.isEmpty(scanPackages))
		{
			return Collections.EMPTY_SET;
		}
		//验证及排重包路径,避免父子路径多次扫描
		Set<String> packages = checkPackage(scanPackages);
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
		Set<String> clazzSet = new HashSet<String>();
		for (String basePackage : packages)
		{
			if (StringUtils.isEmpty(basePackage))
			{
				continue;
			}
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ org.springframework.util.ClassUtils.convertClassNameToResourcePath(
							SystemPropertyUtils.resolvePlaceholders(basePackage))
					+ "/" + DEFAULT_RESOURCE_PATTERN;
			try
			{
				Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
				for (Resource resource : resources)
				{
					//检查resource，这里的resource都是class
					String clazz = loadClassName(metadataReaderFactory, resource);
					clazzSet.add(clazz);
				}
			}
			catch (Exception e)
			{
				LOG.error("获取包下面的类信息失败,package:" + basePackage, e);
			}

		}
		return clazzSet;
	}

	/**
	 * 排重、检测package父子关系，避免多次扫描
	 *
	 * @param scanPackages
	 * @return 返回检查后有效的路径集合
	 */
	private static Set<String> checkPackage(String scanPackages)
	{
		if (StringUtils.isEmpty(scanPackages))
		{
			return Collections.EMPTY_SET;
		}
		Set<String> packages = new HashSet<String>();
		//排重路径
		Collections.addAll(packages, scanPackages.split(","));
		for (String pInArr : packages.toArray(new String[packages.size()]))
		{
			if (StringUtils.isEmpty(pInArr) || pInArr.equals(".") || pInArr.startsWith("."))
			{
				continue;
			}
			if (pInArr.endsWith("."))
			{
				pInArr = pInArr.substring(0, pInArr.length() - 1);
			}
			Iterator<String> packageIte = packages.iterator();
			boolean needAdd = true;
			while (packageIte.hasNext())
			{
				String pack = packageIte.next();
				if (pInArr.startsWith(pack + "."))
				{
					//如果待加入的路径是已经加入的pack的子集，不加入
					needAdd = false;
				}
				else if (pack.startsWith(pInArr + "."))
				{
					//如果待加入的路径是已经加入的pack的父集，删除已加入的pack
					packageIte.remove();
				}
			}
			if (needAdd)
			{
				packages.add(pInArr);
			}
		}
		return packages;
	}

	/**
	 * 加载资源，根据resource获取className
	 *
	 * @param metadataReaderFactory spring中用来读取resource为class的工具
	 * @param resource              这里的资源就是一个Class
	 * @throws IOException
	 */
	private static String loadClassName(MetadataReaderFactory metadataReaderFactory, Resource resource)
			throws IOException
	{
		try
		{
			if (resource.isReadable())
			{
				MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
				if (metadataReader != null)
				{
					return metadataReader.getClassMetadata().getClassName();
				}
			}
		}
		catch (Exception e)
		{
			LOG.error("根据resource获取类名称失败", e);
		}
		return null;
	}

	/**
	 * 把action下面的所有method遍历一次，标记他们是否需要进行敏感词验证
	 * 如果需要，放入cache中
	 *
	 * @param fullClassName
	 */
	public static Set<Method> findAnnotationMethods(String fullClassName, Class<? extends Annotation> anno)
			throws ClassNotFoundException
	{
		Set<Method> methodSet = new HashSet<Method>();
		Class<?> clz = Class.forName(fullClassName);
		Method[] methods = clz.getDeclaredMethods();
		for (Method method : methods)
		{
			if (method.getModifiers() != Modifier.PUBLIC)
			{
				continue;
			}
			Annotation annotation = method.getAnnotation(anno);
			if (annotation != null)
			{
				methodSet.add(method);
			}
		}
		return methodSet;
	}

	public static boolean findAnnotationClasses(String fullClassName, Class<? extends Annotation> anno)
			throws ClassNotFoundException
	{
		Class<?> clz = Class.forName(fullClassName);
		Annotation annotation = clz.getAnnotation(anno);
		if (annotation != null)
		{
			//			System.out.println(fullClassName + " is true");
			return true;
		}
		return false;
	}

	public static List<MQConsumerConfigBean> genConsumerConfigList(String packages)
	{
		Map<String, MQConsumerConfigBean> consumerConfigs = Maps.newHashMap();
		Set<String> consumerCallbackNames = findAnnotationClass(packages, MQConsumerMeta.class);
		ArrayList<String> consumerCallbackNamesList = new ArrayList<>(consumerCallbackNames);
		Collections.sort(consumerCallbackNamesList);
		//System.out.println("annotation class:" + consumerCallbackNames);
		for (String callbackName : consumerCallbackNamesList)
		{
			try
			{
				Class<?> cc = Class.forName(callbackName);
				Class<?> interfaceClass = Class.forName(MQConstant.CONSUMER_INTERFACE_CLASSNAME);
				boolean rightStatus = Arrays.asList(cc.getInterfaces()).contains(interfaceClass);
				if (!rightStatus)
				{
					LOG.fatal("easymq wrong. class:" + callbackName + " not implemets"
							+ MQConstant.CONSUMER_INTERFACE_CLASSNAME);
					continue;
				}
				MQConsumerMeta meta = cc.getAnnotation(MQConsumerMeta.class);
				if (meta == null)
				{
					LOG.fatal("easymq wrong. class:" + callbackName + " annotation analysis wrong.");
					continue;
				}
				MQRecMsgHandler handler = (MQRecMsgHandler) cc.newInstance();
				consumerConfigs = genConsumerConfigList(consumerConfigs, meta, handler);
			}
			catch (Exception e)
			{
				LOG.fatal("easymq wrong. analysis class:" + callbackName, e);
			}
		}
		return new ArrayList<>(consumerConfigs.values());
	}

	/**
	 * 每组一个配置,返回map的键为groupName
	 * @param consumerConfigs
	 * @param meta
	 * @param handler
	 * @return
	 */
	private static Map<String, MQConsumerConfigBean> genConsumerConfigList(
			Map<String, MQConsumerConfigBean> consumerConfigs, MQConsumerMeta meta, MQRecMsgHandler handler)
	{
		if (consumerConfigs == null)
		{
			consumerConfigs = Maps.newHashMap();
		}
		String topic = meta.topic();
		String group = meta.group();
		boolean orderly = meta.isOrderly();
		boolean broadcast = meta.isBroadcast();
		int consumerThreadCount = meta.consumerThreadCount();
		String groupName = genGroupName(group, orderly, broadcast);
		if (consumerConfigs.containsKey(groupName))
		{
			//该组配置存在，更新
			MQConsumerConfigBean configBean = consumerConfigs.get(groupName);
			Map<String, List<MQRecMsgHandler>> topicHandler = configBean.getTopicHandler();
			if (topicHandler == null)
			{
				String warn = "easymq wrong. topic handler is null. topic:" + topic;
				LOG.fatal(warn);
				throw new MqConsumerConfigException(warn);
			}
			if (topicHandler.containsKey(topic))
			{
				List<MQRecMsgHandler> handlers = topicHandler.get(topic);
				handlers.add(handler);
			}
			else
			{
				List<MQRecMsgHandler> handlers = Lists.newArrayList();
				handlers.add(handler);
				topicHandler.put(topic, handlers);
			}
			if (consumerThreadCount > configBean.getConsumerThreadCount())
			{
				configBean.setConsumerThreadCount(consumerThreadCount);
			}
		}
		else
		{
			//该组配置不存在，生成
			MQConsumerConfigBean configBean = new MQConsumerConfigBean();
			configBean.setGroupName(groupName);
			configBean.setConsumerThreadCount(consumerThreadCount);
			configBean.setGroup(group);
			configBean.setOrderly(orderly);
			configBean.setBroadcast(broadcast);
			Map<String, List<MQRecMsgHandler>> topicHandler = Maps.newHashMap();
			List<MQRecMsgHandler> handlers = Lists.newArrayList();
			handlers.add(handler);
			topicHandler.put(topic, handlers);
			configBean.setTopicHandler(topicHandler);
			consumerConfigs.put(groupName, configBean);
		}
		return consumerConfigs;
	}

	private static String genGroupName(String group, boolean orderly, boolean broadcast)
	{
		String groupName = group;
		if (orderly)
		{
			groupName = groupName + MQConstant.CONSUMER_GROUPNAME_SEP + MQConstant.CONSUMER_ORDERLY_TRUE;
		}
		if (broadcast)
		{
			groupName = groupName + MQConstant.CONSUMER_GROUPNAME_SEP + MQConstant.CONSUMER_BROADCAST_TRUE;
		}
		return groupName;
	}
}
