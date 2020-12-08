[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.org/cn-src/snippets-java.svg?branch=master)](https://travis-ci.org/cn-src/snippets-java)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/2544a8647cde45598ec7fe0dd1cf76c5)](https://www.codacy.com/manual/cn-src/snippets-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cn-src/snippets-java&amp;utm_campaign=Badge_Grade)
[![codecov](https://codecov.io/gh/cn-src/snippets-java/branch/master/graph/badge.svg)](https://codecov.io/gh/cn-src/snippets-java)
[![](https://jitpack.io/v/cn-src/snippets-java.svg)](https://jitpack.io/#cn-src/snippets-java)
![Lines of code](https://img.shields.io/tokei/lines/github/cn-src/snippets-java)

***
# Snippets Java
> 随时提炼一些 Java 代码片段，直接集成使用。
***

## jOOQ 扩展

* 使用 `ConditionBuilder` 可动态构建 where 条件
* 使用 `ConditionCreator` 和 `@Condition` 系列注解可构建样例查询
* 使用 `cn.javaer.snippets.jooq.PGDSL` 扩展支持了 PostgreSQL 相关函数
* 使用 `Geometry` 简单支持了几何对象
* 使用 `JsonbField` 扩展 PostgreSQL 的 JSONB 支持
* 使用 `SimpleJooqJdbcRepository` 将 jOOQ 和 Spring Data JDBC 集成
* `@DataJooqJdbcTest` JooqJdbcRepository 测试支持
* `SnippetsGenerator` `TablesGenerator` jOOQ 自带代码生成器扩展
* `CodeGenTool` 全新的 jOOQ 代码生成器，无需链接数据库，基于实体类生成，支持 spring data 注解

## easy-batch 扩展

* 增加时间增量同步
* 同步记录持久化到数据库
* 实时监控信息

## jackson

* 使用 `cn.javaer.snippets.spring.jackson.Json` 便捷的 JSON 工具
* 支持 jOOQ 相关类型的序列化

## Kryo 序列化

* 支持 Eclipse Collections 集合序列化支持
* 支持不可变 POJO 对象序列化
* 使用 `KryoPool` Kryo 对象池

## 日期格式化扩展

* 自定义 `@DateFillFormat`, `@DateMaxTime`, `@DateMinTime` 等日期格式化注解，可自动填充时间部分和日期偏移
* 支持 spring 和 jackson

## Tree 模型

* `TreeNode` 支持树状结构，主要用于 UI 的树结构相关组件
* 支持与 POJO 的互相转换

## 组合模型

> 如果遵循严格的不可变对象设计，但有时候需要附加属性或对象时。
> 组合模型可以存放对象和附加属性或对象，在 JSON 序列化时如同序列化成一个对象。
> 受 jackson `@JsonUnwrapped` 注解的限制，组合对象只适用于序列化，不适用于反序列化。
> 配合 `MergeUtils` 使用。

* `Assembler` 组合对象
* `DynamicAssembler` 组合动态对象
* `Creator` 添加 `createdBy` 属性对象
* `Auditor` 添加 `updatedBy` `createdBy` 属性对象

## p6spy SQL日志扩展

* p6spy 是驱动级别的统一输出 SQL 日志，无论你混合使用何种基于 JDBC 的框架
* p6spy 可输出填充 SQL 参数的 SQL 日志
* 复制使用 hibernate 的一个源码，扩展了 p6spy 使 SQL 日志带缩进的漂亮格式化

## SpringDoc

* 扩展 Pageable 和 Page 支持
* 扩展支持 Controller 的异常声明来自动生成异常响应文档

## Spring

* 自定义更便捷的 Spring 事务注解，默认 `rollbackFor = Throwable.class` 来避免意外的受检查异常
* 完善的 spring web 异常处理机制，消息国际化

## Spring Data

* 扩展 Eclipse Collections 支持，返回值可以使用 Eclipse Collections 的集合类型
