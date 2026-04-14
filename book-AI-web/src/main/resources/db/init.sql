-- 智慧书城数据库初始化脚本

-- 用户表
CREATE TABLE IF NOT EXISTS "user" (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role INTEGER DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "user" IS '用户表: role 0-普通用户, 1-管理员, 2-VIP1, 3-VIP2, 4-VIP3';

-- 用户收货地址表
CREATE TABLE IF NOT EXISTS user_address (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    receiver_name VARCHAR(50) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    province VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    district VARCHAR(50) NOT NULL,
    detail_address VARCHAR(200) NOT NULL,
    is_default INTEGER DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE user_address IS '用户收货地址表: is_default 0-否, 1-是';

-- 书籍分类表
CREATE TABLE IF NOT EXISTS book_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    parent_id BIGINT DEFAULT 0,
    level INTEGER DEFAULT 1,
    sort INTEGER DEFAULT 0,
    icon VARCHAR(200),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE book_category IS '书籍分类表';

-- 书籍信息表
CREATE TABLE IF NOT EXISTS book_info (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(50),
    publisher VARCHAR(100),
    category_id BIGINT REFERENCES book_category(id),
    cover VARCHAR(500),
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INTEGER DEFAULT 0,
    sales INTEGER DEFAULT 0,
    is_on_sale INTEGER DEFAULT 1,
    publish_date TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE book_info IS '书籍信息表: is_on_sale 0-下架, 1-上架';

-- 书籍章节表
CREATE TABLE IF NOT EXISTS book_chapter (
    id BIGSERIAL PRIMARY KEY,
    book_id BIGINT NOT NULL REFERENCES book_info(id) ON DELETE CASCADE,
    title VARCHAR(100) NOT NULL,
    chapter_num INTEGER NOT NULL,
    content TEXT,
    is_free INTEGER DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE book_chapter IS '书籍章节表: is_free 0-付费, 1-免费';

-- 购物车表
CREATE TABLE IF NOT EXISTS cart_item (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    book_id BIGINT NOT NULL REFERENCES book_info(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL DEFAULT 1,
    price DECIMAL(10, 2),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, book_id)
);

COMMENT ON TABLE cart_item IS '购物车表';

-- 订单信息表
CREATE TABLE IF NOT EXISTS order_info (
    id BIGSERIAL PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL REFERENCES "user"(id),
    address_id BIGINT NOT NULL REFERENCES user_address(id),
    total_amount DECIMAL(10, 2) NOT NULL,
    discount_amount DECIMAL(10, 2) DEFAULT 0,
    pay_amount DECIMAL(10, 2) NOT NULL,
    status INTEGER DEFAULT 0,
    remark VARCHAR(500),
    pay_time TIMESTAMP,
    ship_time TIMESTAMP,
    receive_time TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE order_info IS '订单信息表: status 0-待付款, 1-已付款, 2-已发货, 3-已送达, 4-已完成, 5-已取消, 6-已退款';

-- 订单明细表
CREATE TABLE IF NOT EXISTS order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES order_info(id) ON DELETE CASCADE,
    book_id BIGINT NOT NULL,
    book_title VARCHAR(100),
    book_cover VARCHAR(500),
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE order_item IS '订单明细表';

-- 论坛板块表
CREATE TABLE IF NOT EXISTS forum_section (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    icon VARCHAR(200),
    sort INTEGER DEFAULT 0,
    post_count INTEGER DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE forum_section IS '论坛板块表';

-- 论坛帖子表
CREATE TABLE IF NOT EXISTS forum_post (
    id BIGSERIAL PRIMARY KEY,
    section_id BIGINT NOT NULL REFERENCES forum_section(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    view_count INTEGER DEFAULT 0,
    reply_count INTEGER DEFAULT 0,
    like_count INTEGER DEFAULT 0,
    is_top INTEGER DEFAULT 0,
    is_essence INTEGER DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE forum_post IS '论坛帖子表: is_top 0-否, 1-是; is_essence 0-否, 1-是';

-- 插入默认管理员 (密码: admin123)
INSERT INTO "user" (username, password, phone, role, create_time, update_time)
VALUES ('admin', '$2a$12$NQa3IoO1sAOzDYNzT12bO.1nA0x8EY3Zqz5wFLqMrZgUHMz4wq1kC', '13800138000', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;

-- 插入默认分类数据
INSERT INTO book_category (name, parent_id, level, sort, icon) VALUES
('文学', 0, 1, 1, 'icon-literature'),
('小说', 0, 1, 2, 'icon-novel'),
('教育', 0, 1, 3, 'icon-education'),
('科技', 0, 1, 4, 'icon-tech')
ON CONFLICT DO NOTHING;

-- 插入默认板块数据
INSERT INTO forum_section (name, description, icon, sort) VALUES
('书评交流', '分享您的读书心得', 'icon-review', 1),
('作家专栏', '与作家近距离交流', 'icon-author', 2),
('读书活动', '参与各类读书活动', 'icon-activity', 3),
('求助问答', '有问题来这里提问', 'icon-qa', 4)
ON CONFLICT DO NOTHING;

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_user_username ON "user"(username);
CREATE INDEX IF NOT EXISTS idx_user_role ON "user"(role);
CREATE INDEX IF NOT EXISTS idx_address_user_id ON user_address(user_id);
CREATE INDEX IF NOT EXISTS idx_book_category_id ON book_info(category_id);
CREATE INDEX IF NOT EXISTS idx_book_is_on_sale ON book_info(is_on_sale);
CREATE INDEX IF NOT EXISTS idx_chapter_book_id ON book_chapter(book_id);
CREATE INDEX IF NOT EXISTS idx_cart_user_id ON cart_item(user_id);
CREATE INDEX IF NOT EXISTS idx_order_user_id ON order_info(user_id);
CREATE INDEX IF NOT EXISTS idx_order_status ON order_info(status);
CREATE INDEX IF NOT EXISTS idx_order_item_order_id ON order_item(order_id);
CREATE INDEX IF NOT EXISTS idx_post_section_id ON forum_post(section_id);
CREATE INDEX IF NOT EXISTS idx_post_user_id ON forum_post(user_id);
CREATE INDEX IF NOT EXISTS idx_post_is_top ON forum_post(is_top);
