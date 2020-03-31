package edu.nju.se.teamnamecannotbeempty.backend.service.search;

import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
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
     *
     * @param keywords 关键词列表
     * @param mode     搜索模式，皆为Bean，请用Bean的方式来获取
     * @param pageable 分页模式，请自行了解；页数从0开始
     * @param sortMode 排序模式，也皆为Bean
     * @return 对应的搜索结果页
     * @throws org.hibernate.search.exception.EmptyQueryException，勿传入空字符串
     * @throws IllegalArgumentException，如果任意参数为null
     */
    @Deprecated
    Page<Paper> search(List<String> keywords, SearchMode mode, Pageable pageable, SortMode sortMode);

    /**
     * 用搜索关键词串（可以是用户的原始输入）、搜索模式和分页模式来搜索
     *
     * @param keywords 关键词字符串
     * @param mode     搜索模式，皆为Bean，请用Bean的方式来获取
     * @param pageable 分页模式，请自行了解；页数从0开始
     * @param sortMode 排序模式，也皆为Bean
     * @return 对应的搜索结果页
     * @throws org.hibernate.search.exception.EmptyQueryException，勿传入空字符串
     * @throws IllegalArgumentException，如果任意参数为null
     */
    Page<Paper> search(String keywords, SearchMode mode, Pageable pageable, SortMode sortMode);

    /**
     * 用搜索关键词串（可以是用户的原始输入）、搜索模式和分页模式来搜索，默认使用relevance排序
     *
     * @param keywords 关键词字符串
     * @param mode     搜索模式，皆为Bean，请用Bean的方式来获取
     * @param pageable 分页模式，请自行了解；页数从0开始
     * @return 对应的搜索结果页
     * @throws org.hibernate.search.exception.EmptyQueryException，勿传入空字符串
     * @throws IllegalArgumentException，如果任意参数为null
     */
    Page<Paper> search(String keywords, SearchMode mode, Pageable pageable);
}

