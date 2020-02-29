package edu.nju.se.teamnamecannotbeempty.backend.service.search;

import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 搜索的业务逻辑
 */
public interface SearchService {
    /**
     * 用搜索关键词列表、搜索模式和分页模式来搜索
     * 该方法不推荐使用
     * @param keywords 关键词列表
     * @param mode 搜索模式，皆为Bean，请用Bean的方式来获取
     * @param pageable 分页模式，请自行了解
     * @return 对应的搜索结果页
     */
    Page<Paper> search(List<String> keywords, SearchMode mode, Pageable pageable);

    /**
     * 用搜索关键词串（可以是用户的原始输入）、搜索模式和分页模式来搜索
     * @param keywords 关键词字符串
     * @param mode 搜索模式，皆为Bean，请用Bean的方式来获取
     * @param pageable 分页模式，请自行了解
     * @return 对应的搜索结果页
     */
    Page<Paper> search(String keywords, SearchMode mode, Pageable pageable);
}

