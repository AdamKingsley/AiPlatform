/*******************************************************************************
 * Copyright (c) 2016-2017 天泽信息 www.tiza.com.cn
 *
 *******************************************************************************/

package cn.edu.nju.software.util;

import cn.edu.nju.software.util.annotation.Nullable;

import javax.validation.constraints.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数校验统一使用Apache Common Lange Validate, 补充一些缺少的.
 * 
 * 为什么不用Guava的Preconditions? 无他，
 * 
 * 一是少打几个字而已， 二是Validate的方法多，比如noNullElements()判断多个元素都不为空
 * 
 * com.google.common.math.MathPreconditions
 *
 */
public abstract class Validate extends org.apache.commons.lang3.Validate {

	private static final String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

	/**
	 * 校验为正数则返回该数字，否则抛出异常.
	 */
	public static int positive(@Nullable String role, int x) {
		if (x <= 0) {
			throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
		}
		return x;
	}

	/**
	 * 校验为正数则返回该数字，否则抛出异常.
	 */
	public static Integer positive(@Nullable String role, Integer x) {
		if (x.intValue() <= 0) {
			throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
		}
		return x;
	}

	/**
	 * 校验为正数则返回该数字，否则抛出异常.
	 */
	public static long positive(@Nullable String role, long x) {
		if (x <= 0) {
			throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
		}
		return x;
	}

	/**
	 * 校验为正数则返回该数字，否则抛出异常.
	 */
	public static Long positive(@Nullable String role, Long x) {
		if (x.longValue() <= 0) {
			throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
		}
		return x;
	}

	/**
	 * 校验为正数则返回该数字，否则抛出异常.
	 */
	public static double positive(@Nullable String role, double x) {
		if (!(x > 0)) { // not x < 0, to work with NaN.
			throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
		}
		return x;
	}

	/**
	 * 校验为正数则返回该数字，否则抛出异常.
	 */
	public static int nonNegative(@Nullable String role, int x) {
		if (x < 0) {
			throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
		}
		return x;
	}

	/**
	 * 校验为正数则返回该数字，否则抛出异常.
	 */
	public static Integer nonNegative(@Nullable String role, Integer x) {
		if (x.intValue() < 0) {
			throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
		}
		return x;
	}

	/**
	 * 校验为正数则返回该数字，否则抛出异常.
	 */
	public static long nonNegative(@Nullable String role, long x) {
		if (x < 0) {
			throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
		}
		return x;
	}

	/**
	 * 校验为正数则返回该数字，否则抛出异常.
	 */
	public static Long nonNegative(@Nullable String role, Long x) {
		if (x.longValue() < 0) {
			throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
		}
		return x;
	}

	/**
	 * 校验为正数则返回该数字，否则抛出异常.
	 */
	public static double nonNegative(@Nullable String role, double x) {
		if (!(x >= 0)) { // not x < 0, to work with NaN.
			throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
		}
		return x;
	}

	/**
	 * 校验是否为邮箱
	 */
	public static boolean checkMail(String mail){
		//正则表达式的模式
		Pattern p = Pattern.compile(RULE_EMAIL);
		//正则表达式的匹配器
		Matcher m = p.matcher(mail);
		//进行正则匹配
		return m.matches();
	}
}
