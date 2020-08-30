[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.org/cn-src/snippets-java.svg?branch=master)](https://travis-ci.org/cn-src/snippets-java)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/2544a8647cde45598ec7fe0dd1cf76c5)](https://www.codacy.com/manual/cn-src/snippets-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cn-src/snippets-java&amp;utm_campaign=Badge_Grade)
[![codecov](https://codecov.io/gh/cn-src/snippets-java/branch/master/graph/badge.svg)](https://codecov.io/gh/cn-src/snippets-java)
[![](https://jitpack.io/v/cn-src/snippets-java.svg)](https://jitpack.io/#cn-src/snippets-java)

# Snippets Java
> 一些零散的Java代码片段，集成 jar 后可直接使用

## jOOQ 扩展
* 使用 `ConditionBuilder` 可动态构建 where 条件
* 使用 `ConditionCreator` 和 `@Condition` 系列注解可构建样例查询
* 使用 `cn.javaer.snippets.jooq.SQL` 扩展支持了 PostgreSQL 相关函数
* 使用 `Geometry` 简单支持了几何对象
* 使用 `JsonbField` 扩展 PostgreSQL 的 JSONB 支持
* 使用 `JsonbField` 扩展 PostgreSQL 的 JSONB 支持
* 使用 `SimpleJooqJdbcRepository` 将 jOOQ 和 Spring Data JDBC 集成

## Kryo 序列化
* 支持 Eclipse Collections 集合序列化支持

## Spring Data
* 扩展 Eclipse Collections 支持，返回值可以使用 Eclipse Collections 的集合类型

* 自定义 Spring 事务注解支持
* 完善的异常处理机制，消息国际化，OpenApi异常响应文档自动生成
* p6spy SQL 缩进格式化支持
* 请求/响应 详细日志支持
* 日期填充时间的转换支持
* Tree UI 数据结构支持
* 其它

[文档](https://cn-src.gitee.io/snippets-java/)