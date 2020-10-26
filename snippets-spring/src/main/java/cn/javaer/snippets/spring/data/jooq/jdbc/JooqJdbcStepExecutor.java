package cn.javaer.snippets.spring.data.jooq.jdbc;

import cn.javaer.snippets.spring.data.jooq.QueryStep;
import org.jooq.Condition;
import org.jooq.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * @author cn-src
 */
@NoRepositoryBean
public interface JooqJdbcStepExecutor<T> {

    /**
     * 查询单个实体
     *
     * @param condition 查询条件
     *
     * @return 单个实体
     */
    Optional<T> findOne(Condition condition);

    /**
     * 查询多个实体
     *
     * @param condition 查询条件
     *
     * @return 实体 List
     */
    List<T> findAll(Condition condition);

    /**
     * 查询多个实体
     *
     * @param condition 查询条件
     * @param sort 排序条件
     *
     * @return 实体 List
     */
    List<T> findAll(Condition condition, Sort sort);

    /**
     * 分页查询多个实体
     *
     * @param condition 查询条件
     * @param pageable 分页参数
     *
     * @return 分页结果
     */
    Page<T> findAll(Condition condition, Pageable pageable);

    /**
     * 统计实体总数
     *
     * @param condition 查询条件
     *
     * @return 总数
     */
    long count(Condition condition);

    /**
     * 判断是否存在实体
     *
     * @param condition 查询条件
     *
     * @return 存在返回 true
     */
    boolean exists(Condition condition);

    /**
     * 查询多个实体
     *
     * @param queryStep jOOQ 查询
     *
     * @return 实体 List
     */
    List<T> findAll(QueryStep queryStep);

    /**
     * 查询多个实体
     *
     * @param query jOOQ 查询
     *
     * @return 实体 List
     */
    List<T> findAll(Query query);
}
