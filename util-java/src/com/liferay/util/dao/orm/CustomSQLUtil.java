/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.util.dao.orm;

import com.liferay.portal.kernel.dao.orm.QueryDefinition;
import com.liferay.portal.kernel.dao.orm.WildcardMode;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.sql.SQLException;

/**
 * @author Brian Wing Shun Chan
 * @author Bruno Farache
 * @author Raymond Augé
 * @see com.liferay.portal.dao.orm.custom.sql.CustomSQLUtil
 */
public class CustomSQLUtil {

	public static String appendCriteria(String sql, String criteria) {
		return _customSQL.appendCriteria(sql, criteria);
	}

	public static String get(String id) {
		return _customSQL.get(id);
	}

	public static String get(String id, QueryDefinition<?> queryDefinition) {
		return _customSQL.get(id, queryDefinition);
	}

	public static String get(
		String id, QueryDefinition<?> queryDefinition, String tableName) {

		return _customSQL.get(id, queryDefinition, tableName);
	}

	public static boolean isVendorDB2() {
		return _customSQL.isVendorDB2();
	}

	public static boolean isVendorHSQL() {
		return _customSQL.isVendorHSQL();
	}

	public static boolean isVendorInformix() {
		return _customSQL.isVendorInformix();
	}

	public static boolean isVendorMySQL() {
		return _customSQL.isVendorMySQL();
	}

	public static boolean isVendorOracle() {
		return _customSQL.isVendorOracle();
	}

	public static boolean isVendorSybase() {
		return _customSQL.isVendorSybase();
	}

	public static String[] keywords(String keywords) {
		return _customSQL.keywords(keywords);
	}

	public static String[] keywords(String keywords, boolean lowerCase) {
		return _customSQL.keywords(keywords, lowerCase);
	}

	public static String[] keywords(
		String keywords, boolean lowerCase, WildcardMode wildcardMode) {

		return _customSQL.keywords(keywords, lowerCase, wildcardMode);
	}

	public static String[] keywords(
		String keywords, WildcardMode wildcardMode) {

		return _customSQL.keywords(keywords, wildcardMode);
	}

	public static String[] keywords(String[] keywordsArray) {
		return _customSQL.keywords(keywordsArray);
	}

	public static String[] keywords(String[] keywordsArray, boolean lowerCase) {
		return _customSQL.keywords(keywordsArray, lowerCase);
	}

	public static void reloadCustomSQL() throws SQLException {
		_customSQL.reloadCustomSQL();
	}

	public static String removeGroupBy(String sql) {
		return _customSQL.removeGroupBy(sql);
	}

	public static String removeOrderBy(String sql) {
		return _customSQL.removeOrderBy(sql);
	}

	public static String replaceAndOperator(String sql, boolean andOperator) {
		return _customSQL.replaceAndOperator(sql, andOperator);
	}

	public static String replaceGroupBy(String sql, String groupBy) {
		return _customSQL.replaceGroupBy(sql, groupBy);
	}

	public static String replaceIsNull(String sql) {
		return _customSQL.replaceIsNull(sql);
	}

	public static String replaceKeywords(
		String sql, String field, boolean last, int[] values) {

		return _customSQL.replaceKeywords(sql, field, last, values);
	}

	public static String replaceKeywords(
		String sql, String field, boolean last, long[] values) {

		return _customSQL.replaceKeywords(sql, field, last, values);
	}

	public static String replaceKeywords(
		String sql, String field, String operator, boolean last,
		String[] values) {

		return _customSQL.replaceKeywords(sql, field, operator, last, values);
	}

	public static String replaceOrderBy(String sql, OrderByComparator<?> obc) {
		return _customSQL.replaceOrderBy(sql, obc);
	}

	public void setCustomSQL(CustomSQL customSQL) {
		_customSQL = customSQL;
	}

	private static CustomSQL _customSQL;

}