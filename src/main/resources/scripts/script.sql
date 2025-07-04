INSERT INTO sources (id, name, news_feed_url, is_active,
                     article_element_selector, article_element_attribute,
                     category_selector, category_attribute,
                     content_selector, content_attribute,
                     keyword_selector, keyword_attribute,
                     published_at_selector, published_at_attribute,
                     date_time_format,
                     short_description_selector, short_description_attribute,
                     title_selector, title_attribute)
VALUES (1, 'RIA Новости', 'https://ria.ru/lenta/', true,
        'span[itemprop=itemListElement] a[itemprop=url]', null,
        'meta[name=analytics:rubric]', 'content',
        'div[itemprop=articleBody]', null,
        'meta[name=keywords]', 'content',
        'meta[property=article:published_time]', 'content',
        'yyyyMMdd''T''HHmm',
        'meta[name=description]', 'content',
        'meta[name=analytics:title]', 'content'),
       (2, 'Лента', 'https://lenta.ru/parts/news/', true,
        'li.parts-page__item > a.card-full-news._parts-news', null,
        'div.rubric-header__title', null,
        'p.topic-body__content-text', null,
        'div.rubric-header__title', null,
        'a.topic-header__time', null,
        'HH:mm, d MMMM yyyy',
        'meta[name=description]', 'content',
        'meta[property=og:title]', 'content'),
       (3, 'Газета', 'https://www.gazeta.ru/news/', false,
        'a.m_techlisting', null,
        'a[data-tag-type=master-tag]', 'data-tag-nick',
        'div[itemprop=articleBody]', null,
        'meta[name=keywords]', 'content',
        'time[itemprop=datePublished]', 'datetime',
        'yyyy-MM-dd''T''HH:mm:ssXXX',
        'meta[name=description]', 'content',
        'title', null);

INSERT INTO settings (id, type_id, value) VALUES (1, 1, '0 0 8 * * *'),
                                                 (2, 2, '1');