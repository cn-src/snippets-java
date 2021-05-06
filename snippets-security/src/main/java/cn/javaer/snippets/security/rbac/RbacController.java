package cn.javaer.snippets.security.rbac;

import cn.javaer.snippets.model.Page;
import cn.javaer.snippets.model.PageParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cn-src
 */
@RestController
@RequestMapping
public class RbacController {
    private final RbacJdbcManager rbacJdbcManager;

    public RbacController(final RbacJdbcManager rbacJdbcManager) {
        this.rbacJdbcManager = rbacJdbcManager;
    }

    @GetMapping("")
    public Page<UserEntity> get(final PageParam pageParam) {
        return this.rbacJdbcManager.findAllUsers(pageParam);
    }
}
