# 基于Web的大学毕业生就业情况调查系统开发流程与数据库设计（最终修订版3）

## 一、开发流程


### 1. 需求分析
- **目标**：开发一个支持学生、辅导员、管理员三类用户角色的就业情况调查系统，满足就业信息登记、审核、查询、统计和系统管理的功能需求，确保不同学校和班级的数据隔离。
- **角色与功能**：
  - **学生**：
    - 登录（选择学校、学号、密码、验证码，验证码显示由`system_settings.require_captcha`控制，纯前端验证）。
    - 查看个人信息（学号、姓名、班级、学院、专业、辅导员姓名）。
    - 就业信息登记（姓名、专业、专业类别（理工类、文史类、体育类、艺术类）、企业性质、公司、职位、月薪、入职日期、地区（城市选择））。
    - 提交就业阶段反馈（需就业状态为已就业，需辅导员审核）。
    - 查看就业信息和反馈的审核状态（待审核、审核通过）。
    - 增删改查自己的就业信息和反馈。
  - **辅导员**：
    - 登录（选择学校、工号、密码、验证码，验证码显示由`system_settings.require_captcha`控制，纯前端验证）。
    - 班级信息管理：查看**本班**学生花名册及就业状态（无法查看其他班级）。
    - 审核**本班**学生的就业信息和反馈（默认无编辑/删除权限，需管理员授权）。
    - 统计分析：查询**本班**历年就业信息，统计**本班**就业分布（地区、薪资、企业性质，饼图展示）和就业趋势（折线图展示）。
  - **管理员**：
    - 登录（选择学校、账号、密码，验证码显示由`system_settings.require_captcha`控制，纯前端验证）。
    - 查询**本校**学生就业信息和反馈（无需审核）。
    - 查看**本校**学生花名册及就业状态。
    - 统计分析：查询**本校**历年就业信息，统计**本校**就业分布（地区、薪资、企业性质，饼图展示）和就业趋势（折线图展示）。
    - 系统管理：
      - 用户管理：添加、重置密码、删除**本校**学生和辅导员账号。
      - 角色权限分配：为**本校**辅导员分配编辑/删除权限。
      - 系统参数设置：是否需要验证码、是否需要辅导员审核（仅影响本校）。
- **非功能性需求**：
  - 可视化：使用饼图和折线图展示统计数据。
- **数据隔离**：
  - 不同学校的数据完全隔离，管理员和辅导员只能访问当前登录学校的数据。
  - 不同班级的数据隔离，辅导员只能访问自己管辖班级的学生数据.
- **验证码**：
  - 使用`validcode`开源组件，纯前端验证（如图片验证码或滑动验证）。
  - 是否显示验证码由`system_settings.require_captcha`控制，前端通过`/api/settings`获取设置。

### 2. 系统设计
- **技术栈**：
  - **前端**：Vue3（组件化开发）、Element Plus（UI组件库）、Axios（HTTP请求）、ECharts（图表展示）、validcode（验证码组件）。
  - **后端**：Spring Boot（框架）、MyBatis Plus（ORM）。
  - **数据库**：MySQL（关系型数据库）。
- **系统架构**：
  - **前端**：单页应用（SPA），通过Vue Router实现页面路由，Element Plus提供表单、表格、图表组件，validcode处理验证码。
  - **后端**：RESTful API，Spring Boot提供服务端逻辑，MyBatis Plus处理数据库操作。
  - **数据库**：MySQL存储用户信息、就业信息、反馈、权限等数据，school_id和counselor_id确保数据隔离。
- **模块划分**：
  - **学生端**：登录、个人信息展示、就业信息登记、反馈提交（需已就业）、记录查询。
  - **辅导员端**：登录、班级信息管理（本班花名册及就业状态）、就业信息审核、就业反馈审核、统计分析（本班）。
  - **管理员端**：登录、查询本校数据（花名册、就业信息、反馈）、统计分析（本校）、系统管理（用户、权限、参数）。
- **数据隔离实现**：
  - 登录时验证school_id，确保用户只能访问本校数据。
  - 辅导员查询通过counselor_id关联students表，限制只返回本班数据。
  - 管理员查询通过school_id过滤，仅返回本校数据.
- **验证码实现**：
  - 前端通过`/api/settings`获取`require_captcha`，决定是否显示`ValidCode.vue`。
  - `validcode`组件生成验证码并验证用户输入，无需后端交互。

### 3. 数据库设计
#### 表结构
1. **schools（学校表）**
   - 字段：
     - `school_id` (BIGINT, 主键)：学校ID。
     - `school_name` (VARCHAR(100))：学校名称。
     - `created_at` (DATETIME)：创建时间。
     - `updated_at` (DATETIME)：更新时间。
   - 说明：存储学校信息，用于数据隔离。

2. **users（用户表）**
   - 字段：
     - `user_id` (BIGINT, 主键)：用户ID。
     - `school_id` (BIGINT, 外键)：所属学校ID。
     - `username` (VARCHAR(50))：用户名（学生学号/辅导员工号/管理员账号）。
     - `password` (VARCHAR(100))：明文密码。
     - `role` (ENUM('student', 'counselor', 'admin'))：角色。
     - `created_at` (DATETIME)：创建时间。
     - `updated_at` (DATETIME)：更新时间。
   - 说明：存储所有用户账号信息，school_id确保数据隔离。

3. **students（学生信息表）**
   - 字段：
     - `student_id` (BIGINT, 主键)：学生ID（与users表user_id关联）。
     - `school_id` (BIGINT, 外键)：学校ID。
     - `name` (VARCHAR(50))：姓名。
     - `class_name` (VARCHAR(50))：班级。
     - `college` (VARCHAR(50))：学院。
     - `major` (VARCHAR(50))：专业。
     - `counselor_id` (BIGINT, 外键)：辅导员ID（关联users表）。
     - `employment_status` (ENUM('unemployed', 'employed'))：就业状态。
     - `created_at` (DATETIME)：创建时间。
     - `updated_at` (DATETIME)：更新时间。
   - 说明：存储学生详细信息，counselor_id用于班级数据隔离。

4. **counselors（辅导员信息表）**
   - 字段：
     - `counselor_id` (BIGINT, 主键)：辅导员ID（与users表user_id关联）。
     - `school_id` (BIGINT, 外键)：学校ID。
     - `name` (VARCHAR(50))：姓名。
     - `created_at` (DATETIME)：创建时间。
     - `updated_at` (DATETIME)：更新时间。
   - 说明：存储辅导员信息，school_id确保数据隔离。

5. **employment_records（就业信息表）**
   - 字段：
     - `record_id` (BIGINT, 主键)：记录ID。
     - `student_id` (BIGINT, 外键)：学生ID。
     - `name` (VARCHAR(50))：姓名。
     - `major` (VARCHAR(50))：专业。
     - `major_category` (ENUM('science', 'humanities', 'sports', 'arts'))：专业类别（理工类、文史类、体育类、艺术类）。
     - `company_nature` (VARCHAR(50))：企业性质（如国企、私企）。
     - `company` (VARCHAR(100))：公司名称。
     - `position` (VARCHAR(50))：职位。
     - `salary` (DECIMAL(10,2))：月薪。
     - `entry_date` (DATE)：入职日期。
     - `region` (VARCHAR(50))：地区（城市）。
     - `status` (ENUM('pending', 'approved'))：审核状态（仅辅导员审核）。
     - `created_at` (DATETIME)：创建时间。
     - `updated_at` (DATETIME)：更新时间。
   - 说明：存储学生就业信息，student_id关联students表确保数据隔离。

6. **feedback_records（反馈信息表）**
   - 字段：
     - `feedback_id` (BIGINT, 主键)：反馈ID。
     - `student_id` (BIGINT, 外键)：学生ID。
     - `stage` (VARCHAR(50))：就业阶段（如初期、正式工作）。
     - `content` (TEXT)：反馈内容。
     - `status` (ENUM('pending', 'approved'))：审核状态（仅辅导员审核）。
     - `created_at` (DATETIME)：创建时间。
     - `updated_at` (DATETIME)：更新时间。
   - 说明：存储学生就业阶段反馈（需就业状态为已就业），student_id确保数据隔离。

7. **permissions（权限表）**
   - 字段：
     - `permission_id` (BIGINT, 主键)：权限ID。
     - `user_id` (BIGINT, 外键)：用户ID（辅导员）。
     - `can_edit` (BOOLEAN)：是否可编辑。
     - `can_delete` (BOOLEAN)：是否可删除。
     - `created_at` (DATETIME)：创建时间。
     - `updated_at` (DATETIME)：更新时间。
   - 说明：存储辅导员的额外权限，user_id关联users表。

8. **system_settings（系统设置表）**
   - 字段：
     - `setting_id` (BIGINT, 主键)：设置ID。
     - `school_id` (BIGINT, 外键)：学校ID。
     - `require_captcha` (BOOLEAN)：是否需要验证码（控制前端显示）。
     - `require_approval` (BOOLEAN)：是否需要辅导员审核。
     - `created_at` (DATETIME)：创建时间。
     - `updated_at` (DATETIME)：更新时间。
   - 说明：存储本校系统参数配置，school_id确保隔离。

#### 实体关系
- **schools** 与 **users**：一对多（一个学校有多个用户）。
- **users** 与 **students**：一对一（学生用户对应一个学生信息）。
- **users** 与 **counselors**：一对一（辅导员用户对应一个辅导员信息）。
- **students** 与 **employment_records**：一对多（一个学生有多个就业记录）。
- **students** 与 **feedback_records**：一对多（一个学生有多个反馈记录）。
- **users** 与 **permissions**：一对多（一个辅导员可有多个权限记录）。
- **schools** 与 **system_settings**：一对一（一个学校一个系统设置）。
- **students** 与 **counselors**：多对一（多个学生对应一个辅导员）。

### 4. 项目结构

#### 4.1 前端项目结构（Vue3+Element Plus）
```
src/
├── assets/                    # 静态资源
│   ├── css/                   # 全局样式
│   │   └── global.css         # 全局CSS
│   ├── images/                # 图片资源
│   └── fonts/                 # 字体文件
├── components/                # 公共组件
│   ├── LoginForm.vue          # 登录表单组件（包含学校选择、用户名、密码、验证码）
│   ├── ValidCode.vue          # 验证码组件（基于validcode开源组件，纯前端验证）
│   ├── StatChart.vue          # 统计图表组件（饼图和折线图，基于ECharts）
│   └── DataTable.vue          # 通用表格组件（用于花名册、就业信息、反馈展示）
├── views/                     # 页面组件
│   ├── student/               # 学生端页面
│   │   ├── Dashboard.vue      # 学生仪表盘（个人信息展示）
│   │   ├── Employment.vue     # 就业信息登记与管理
│   │   └── Feedback.vue       # 就业反馈提交与管理
│   ├── counselor/             # 辅导员端页面
│   │   ├── Dashboard.vue      # 辅导员仪表盘（本班花名册及就业状态）
│   │   ├── Employment.vue     # 本班就业信息管理与审核
│   │   ├── Feedback.vue       # 本班反馈信息管理与审核
│   │   └── Statistics.vue     # 本班统计分析（饼图、折线图）
│   ├── admin/                 # 管理员端页面
│   │   ├── Dashboard.vue      # 管理员仪表盘（本校花名册及就业状态）
│   │   ├── Employment.vue     # 本校就业信息查询
│   │   ├── Feedback.vue       # 本校反馈信息查询
│   │   ├── Statistics.vue     # 本校统计分析（饼图、折线图）
│   │   ├── UserManage.vue     # 用户管理（学生/辅导员账号管理）
│   │   ├── Permission.vue     # 权限分配
│   │   └── Settings.vue       # 系统设置（验证码、审核开关）
├── api/                       # API请求封装
│   ├── auth.js                # 认证相关接口（登录、获取系统设置）
│   ├── student.js             # 学生相关接口（信息、就业、反馈）
│   ├── counselor.js           # 辅导员相关接口（花名册、审核、统计）
│   └── admin.js               # 管理员相关接口（查询、管理、统计）
├── router/                    # 路由配置
│   └── index.js               # Vue Router配置（角色动态路由）
├── store/                     # 状态管理（Pinia推荐）
│   └── index.js               # 管理用户状态（user_id, school_id, role）
├── App.vue                    # 根组件
├── main.js                    # 入口文件（初始化Vue、Element Plus、ECharts、validcode）
└── vite.config.js             # Vite配置文件
```

##### 前端结构说明
- **assets/**：存放全局CSS、图片和字体。
- **components/**：
  - `LoginForm.vue`：登录表单，包含学校下拉框（从`/api/schools`获取）、用户名、密码，动态显示`ValidCode.vue`（基于`/api/settings`的`require_captcha`）。
  - `ValidCode.vue`：基于`validcode`开源组件，纯前端生成和验证验证码（如图片验证码或滑动验证），无需后端交互。
  - `StatChart.vue`：封装ECharts，渲染饼图（地区、薪资、企业性质分布）和折线图（就业趋势）。
  - `DataTable.vue`：基于Element Plus的Table组件，展示花名册、就业信息和反馈列表。
- **views/**：
  - 学生端：个人信息展示、就业信息登记（含城市选择）、反馈提交/管理。
  - 辅导员端：本班花名册、就业信息和反馈审核、本班统计图表。
  - 管理员端：本校花名册、就业信息和反馈查询、统计图表、用户管理、权限分配、系统设置。
- **api/**：使用Axios封装后端接口，按角色模块化。
- **router/**：根据角色（student/counselor/admin）动态加载路由，限制访问。
- **store/**：使用Pinia管理用户状态（user_id、school_id、role），确保数据隔离。
- **main.js**：初始化Vue、Element Plus、ECharts、validcode。

#### 4.2 后端项目结构（Spring Boot+MyBatis Plus）
```
src/
├── main/
│   ├── java/
│   │   └── com/example/employment/
│   │       ├── controller/              # 控制器层
│   │       │   ├── AuthController.java  # 认证接口（登录、系统设置）
│   │       │   ├── StudentController.java # 学生接口
│   │       │   ├── CounselorController.java # 辅导员接口
│   │       │   └── AdminController.java # 管理员接口
│   │       ├── service/                 # 业务逻辑层
│   │       │   ├── AuthService.java     # 认证服务
│   │       │   ├── StudentService.java  # 学生服务
│   │       │   ├── CounselorService.java # 辅导员服务
│   │       │   ├── AdminService.java    # 管理员服务
│   │       │   └── StatisticsService.java # 统计服务
│   │       ├── mapper/                  # MyBatis Plus数据访问层
│   │       │   ├── SchoolMapper.java    # 学校表操作
│   │       │   ├── UserMapper.java      # 用户表操作
│   │       │   ├── StudentMapper.java   # 学生信息表操作
│   │       │   ├── CounselorMapper.java # 辅导员信息表操作
│   │       │   ├── EmploymentMapper.java # 就业信息表操作
│   │       │   ├── FeedbackMapper.java  # 反馈信息表操作
│   │       │   ├── PermissionMapper.java # 权限表操作
│   │       │   └── SettingsMapper.java  # 系统设置表操作
│   │       ├── entity/                  # 实体类
│   │       │   ├── School.java          # 学校实体
│   │       │   ├── User.java            # 用户实体
│   │       │   ├── Student.java         # 学生信息实体
│   │       │   ├── Counselor.java       # 辅导员信息实体
│   │       │   ├── EmploymentRecord.java # 就业信息实体
│   │       │   ├── FeedbackRecord.java  # 反馈信息实体
│   │       │   ├── Permission.java      # 权限实体
│   │       │   └── SystemSettings.java  # 系统设置实体
│   │       ├── dto/                     # 数据传输对象
│   │       │   ├── LoginDTO.java        # 登录请求/响应
│   │       │   ├── EmploymentDTO.java   # 就业信息请求/响应
│   │       │   ├── FeedbackDTO.java     # 反馈信息请求/响应
│   │       │   └── StatisticsDTO.java   # 统计数据响应
│   │       ├── config/                  # 配置类
│   │       │   └── MyBatisPlusConfig.java # MyBatis Plus配置
│   │       └── EmploymentApplication.java # Spring Boot主应用
│   ├── resources/
│   │   ├── application.yml              # 配置文件（数据库、端口等）
│   │   ├── mapper/                      # MyBatis XML映射文件
│   │   │   ├── SchoolMapper.xml         # 学校表SQL
│   │   │   ├── UserMapper.xml           # 用户表SQL
│   │   │   ├── StudentMapper.xml        # 学生信息表SQL
│   │   │   ├── CounselorMapper.xml      # 辅导员信息表SQL
│   │   │   ├── EmploymentMapper.xml     # 就业信息表SQL
│   │   │   ├── FeedbackMapper.xml       # 反馈信息表SQL
│   │   │   ├── PermissionMapper.xml     # 权限表SQL
│   │   │   └── SettingsMapper.xml       # 系统设置表SQL
│   │   └── static/                      # 静态资源（可选，前端打包后可放此处）
└── test/
    └── java/
        └── com/example/employment/
            ├── service/                 # 单元测试
            │   ├── AuthServiceTest.java
            │   ├── StudentServiceTest.java
            │   └── CounselorServiceTest.java
            └── controller/              # 集成测试
                ├── AuthControllerTest.java
                ├── StudentControllerTest.java
                └── CounselorControllerTest.java
```

##### 后端结构说明
- **controller/**：处理HTTP请求，按角色划分，与前端API对应。
- **service/**：业务逻辑层，实现数据隔离、就业状态检查、统计计算。
- **mapper/**：MyBatis Plus接口，配合XML文件实现复杂查询（如统计）。
- **entity/**：数据库表对应的Java实体类。
- **dto/**：数据传输对象，定义请求和响应格式。
- **config/**：MyBatis Plus配置，启用分页、自动填充时间字段。
- **resources/mapper/**：MyBatis XML文件，定义复杂SQL查询。
- **resources/application.yml**：配置数据库连接、端口、CORS。

### 5. 接口设计
以下是完整的RESTful API接口设计，确保数据隔离，统计接口固定为当前登录的学校和班级。

#### 5.1 公共接口
- **POST /api/login**
  - 描述：用户登录，验证school_id、username和password。
  - 请求参数：
    ```json
    {
      "school_id": "BIGINT", // 学校ID
      "username": "String", // 用户名（学号/工号/管理员账号）
      "password": "String" // 密码（明文）
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "登录成功",
      "data": {
        "user_id": "BIGINT",
        "role": "student/counselor/admin",
        "school_id": "BIGINT"
      }
    }
    ```

- **GET /api/settings**
  - 描述：获取系统设置（验证码和审核开关）。
  - 请求参数：无（通过会话获取school_id）。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "获取成功",
      "data": {
        "require_captcha": "Boolean",
        "require_approval": "Boolean"
      }
    }
    ```

- **GET /api/schools**
  - 描述：获取学校列表（用于登录页面下拉框）。
  - 请求参数：无。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "获取成功",
      "data": [
        {
          "school_id": "BIGINT",
          "school_name": "String"
        }
      ]
    }
    ```

#### 5.2 学生端接口
- **GET /api/student/info**
  - 描述：获取学生个人信息。
  - 请求参数：无（通过会话获取user_id）。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "获取成功",
      "data": {
        "student_id": "BIGINT",
        "school_id": "BIGINT",
        "name": "String",
        "class_name": "String",
        "college": "String",
        "major": "String",
        "counselor_name": "String",
        "employment_status": "unemployed/employed"
      }
    }
    ```

- **POST /api/employment/register**
  - 描述：学生登记就业信息。
  - 请求参数：
    ```json
    {
      "name": "String",
      "major": "String",
      "major_category": "science/humanities/sports/arts",
      "company_nature": "String",
      "company": "String",
      "position": "String",
      "salary": "Number",
      "entry_date": "YYYY-MM-DD",
      "region": "String" // 城市
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "登记成功",
      "data": {
        "record_id": "BIGINT",
        "status": "pending"
      }
    }
    ```

- **POST /api/feedback/submit**
  - 描述：学生提交就业反馈（需employment_status为employed）。
  - 请求参数：
    ```json
    {
      "stage": "String", // 就业阶段
      "content": "String" // 反馈内容
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "提交成功",
      "data": {
        "feedback_id": "BIGINT",
        "status": "pending"
      }
    }
    ```

- **GET /api/student/employment**
  - 描述：查询学生自己的就业信息。
  - 请求参数：无（通过会话获取user_id）。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "查询成功",
      "data": [
        {
          "record_id": "BIGINT",
          "name": "String",
          "major": "String",
          "major_category": "science/humanities/sports/arts",
          "company_nature": "String",
          "company": "String",
          "position": "String",
          "salary": "Number",
          "entry_date": "YYYY-MM-DD",
          "region": "String",
          "status": "pending/approved",
          "created_at": "DATETIME"
        }
      ]
    }
    ```

- **GET /api/student/feedback**
  - 描述：查询学生自己的反馈记录。
  - 请求参数：无（通过会话获取user_id）。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "查询成功",
      "data": [
        {
          "feedback_id": "BIGINT",
          "stage": "String",
          "content": "String",
          "status": "pending/approved",
          "created_at": "DATETIME"
        }
      ]
    }
    ```

- **PUT /api/employment/update/{record_id}**
  - 描述：学生修改自己的就业信息（仅限pending状态）。
  - 请求参数：
    ```json
    {
      "name": "String",
      "major": "String",
      "major_category": "science/humanities/sports/arts",
      "company_nature": "String",
      "company": "String",
      "position": "String",
      "salary": "Number",
      "entry_date": "YYYY-MM-DD",
      "region": "String"
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "更新成功",
      "data": {
        "record_id": "BIGINT",
        "status": "pending"
      }
    }
    ```

- **PUT /api/feedback/update/{feedback_id}**
  - 描述：学生修改自己的反馈（仅限pending状态）。
  - 请求参数：
    ```json
    {
      "stage": "String",
      "content": "String"
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "更新成功",
      "data": {
        "feedback_id": "BIGINT",
        "status": "pending"
      }
    }
    ```

- **DELETE /api/employment/delete/{record_id}**
  - 描述：学生删除自己的就业信息（仅限pending状态）。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "删除成功"
    }
    ```

- **DELETE /api/feedback/delete/{feedback_id}**
  - 描述：学生删除自己的反馈（仅限pending状态）。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "删除成功"
    }
    ```

#### 5.3 辅导员端接口
- **GET /api/counselor/students**
  - 描述：获取**本班**学生花名册及就业状态（通过counselor_id过滤）。
  - 请求参数：无（通过会话获取counselor_id）。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "查询成功",
      "data": [
        {
          "student_id": "BIGINT",
          "name": "String",
          "class_name": "String",
          "college": "String",
          "major": "String",
          "employment_status": "unemployed/employed"
        }
      ]
    }
    ```

- **GET /api/counselor/employment**
  - 描述：获取**本班**学生就业信息（通过counselor_id过滤）。
  - 请求参数：无（通过会话获取counselor_id）。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "查询成功",
      "data": [
        {
          "record_id": "BIGINT",
          "student_id": "BIGINT",
          "name": "String",
          "major": "String",
          "major_category": "science/humanities/sports/arts",
          "company_nature": "String",
          "company": "String",
          "position": "String",
          "salary": "Number",
          "entry_date": "YYYY-MM-DD",
          "region": "String",
          "status": "pending/approved"
        }
      ]
    }
    ```

- **GET /api/counselor/feedback**
  - 描述：获取**本班**学生反馈信息（通过counselor_id过滤）。
  - 请求参数：无（通过会话获取counselor_id）。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "查询成功",
      "data": [
        {
          "feedback_id": "BIGINT",
          "student_id": "BIGINT",
          "stage": "String",
          "content": "String",
          "status": "pending/approved"
        }
      ]
    }
    ```

- **POST /api/counselor/employment/audit/{record_id}**
  - 描述：审核**本班**学生就业信息（通过counselor_id验证）。
  - 请求参数：
    ```json
    {
      "status": "approved" // 仅支持通过
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "审核成功"
    }
    ```

- **POST /api/counselor/feedback/audit/{feedback_id}**
  - 描述：审核**本班**学生反馈信息（通过counselor_id验证）。
  - 请求参数：
    ```json
    {
      "status": "approved" // 仅支持通过
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "审核成功"
    }
    ```

- **PUT /api/counselor/employment/edit/{record_id}**
  - 描述：辅导员编辑**本班**学生就业信息（需can_edit权限，通过counselor_id验证）。
  - 请求参数：
    ```json
    {
      "name": "String",
      "major": "String",
      "major_category": "science/humanities/sports/arts",
      "company_nature": "String",
      "company": "String",
      "position": "String",
      "salary": "Number",
      "entry_date": "YYYY-MM-DD",
      "region": "String"
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "编辑成功"
    }
    ```

- **DELETE /api/counselor/employment/delete/{record_id}**
  - 描述：辅导员删除**本班**学生就业信息（需can_delete权限，通过counselor_id验证）。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "删除成功"
    }
    ```

- **DELETE /api/counselor/feedback/delete/{feedback_id}**
  - 描述：辅导员删除**本班**学生反馈信息（需can_delete权限，通过counselor_id验证）。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "删除成功"
    }
    ```

- **GET /api/counselor/statistics/history**
  - 描述：查询**本班**历年就业信息（通过counselor_id过滤）。
  - 请求参数：
    ```json
    {
      "year": "Number" // 可选，指定年份
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "查询成功",
      "data": [
        {
          "record_id": "BIGINT",
          "student_id": "BIGINT",
          "name": "String",
          "major": "String",
          "major_category": "science/humanities/sports/arts",
          "company_nature": "String",
          "company": "String",
          "position": "String",
          "salary": "Number",
          "entry_date": "YYYY-MM-DD",
          "region": "String",
          "status": "approved"
        }
      ]
    }
    ```

- **GET /api/counselor/statistics/distribution**
  - 描述：统计**本班**就业分布（地区、薪资、企业性质，饼图数据，通过counselor_id过滤）。
  - 请求参数：
    ```json
    {
      "type": "region/salary/company_nature", // 统计类型
      "year": "Number" // 可选，指定年份
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "统计成功",
      "data": [
        {
          "label": "String", // 地区/薪资范围/企业性质
          "value": "Number" // 数量
        }
      ]
    }
    ```

- **GET /api/counselor/statistics/trend**
  - 描述：统计**本班**就业趋势（折线图数据，通过counselor_id过滤）。
  - 请求参数：
    ```json
    {
      "start_year": "Number",
      "end_year": "Number"
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "统计成功",
      "data": [
        {
          "year": "Number",
          "count": "Number" // 就业人数
        }
      ]
    }
    ```

#### 5.4 管理员端接口
- **GET /api/admin/students**
  - 描述：获取**本校**学生花名册及就业状态（通过school_id过滤）。
  - 请求参数：无（通过会话获取school_id）。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "查询成功",
      "data": [
        {
          "student_id": "BIGINT",
          "name": "String",
          "class_name": "String",
          "college": "String",
          "major": "String",
          "employment_status": "unemployed/employed"
        }
      ]
    }
    ```

- **GET /api/admin/employment**
  - 描述：查询**本校**学生就业信息（通过school_id过滤）。
  - 请求参数：
    ```json
    {
      "year": "Number" // 可选，指定年份
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "查询成功",
      "data": [
        {
          "record_id": "BIGINT",
          "student_id": "BIGINT",
          "name": "String",
          "major": "String",
          "major_category": "science/humanities/sports/arts",
          "company_nature": "String",
          "company": "String",
          "position": "String",
          "salary": "Number",
          "entry_date": "YYYY-MM-DD",
          "region": "String",
          "status": "pending/approved"
        }
      ]
    }
    ```

- **GET /api/admin/feedback**
  - 描述：查询**本校**学生反馈信息（通过school_id过滤）。
  - 请求参数：
    ```json
    {
      "year": "Number" // 可选，指定年份
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "查询成功",
      "data": [
        {
          "feedback_id": "BIGINT",
          "student_id": "BIGINT",
          "stage": "String",
          "content": "String",
          "status": "pending/approved"
        }
      ]
    }
    ```

- **GET /api/admin/statistics/distribution**
  - 描述：统计**本校**就业分布（地区、薪资、企业性质，饼图数据，通过school_id过滤）。
  - 请求参数：
    ```json
    {
      "type": "region/salary/company_nature", // 统计类型
      "year": "Number" // 可选，指定年份
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "统计成功",
      "data": [
        {
          "label": "String", // 地区/薪资范围/企业性质
          "value": "Number" // 数量
        }
      ]
    }
    ```

- **GET /api/admin/statistics/trend**
  - 描述：统计**本校**就业趋势（折线图数据，通过school_id过滤）。
  - 请求参数：
    ```json
    {
      "start_year": "Number",
      "end_year": "Number"
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "统计成功",
      "data": [
        {
          "year": "Number",
          "count": "Number" // 就业人数
        }
      ]
    }
    ```

- **POST /api/admin/user/add**
  - 描述：添加**本校**学生或辅导员账号（通过school_id过滤）。
  - 请求参数：
    ```json
    {
      "username": "String",
      "password": "String",
      "role": "student/counselor",
      "name": "String",
      "class_name": "String", // 学生独有
      "college": "String", // 学生独有
      "major": "String", // 学生独有
      "counselor_id": "BIGINT" // 学生独有，需为本校辅导员
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "添加成功",
      "data": {
        "user_id": "BIGINT"
      }
    }
    ```

- **POST /api/admin/user/reset-password**
  - 描述：重置**本校**学生或辅导员密码（通过school_id过滤）。
  - 请求参数：
    ```json
    {
      "user_id": "BIGINT",
      "new_password": "String"
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "重置成功"
    }
    ```

- **DELETE /api/admin/user/delete/{user_id}**
  - 描述：删除**本校**学生或辅导员账号（通过school_id过滤）。
  - 响应：
    ```json
    {
      "code": 200,
      "message": "删除成功"
    }
    ```

- **POST /api/admin/permission/assign**
  - 描述：为**本校**辅导员分配权限（通过school_id过滤）。
  - 请求参数：
    ```json
    {
      "user_id": "BIGINT",
      "can_edit": "Boolean",
      "can_delete": "Boolean"
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "分配成功"
    }
    ```

- **POST /api/admin/settings/update**
  - 描述：更新**本校**系统设置（通过school_id过滤）。
  - 请求参数：
    ```json
    {
      "require_captcha": "Boolean",
      "require_approval": "Boolean"
    }
    ```
  - 响应：
    ```json
    {
      "code": 200,
      "message": "更新成功"
    }
    ```

### 6. 开发实现
- **前端（Vue3+Element Plus）**：
  - **验证码实现**：
    - `LoginForm.vue`调用`/api/settings`获取`require_captcha`，动态显示`ValidCode.vue`。
    - `ValidCode.vue`使用`validcode`组件生成验证码（图片或滑动验证），前端验证用户输入。
  - **数据隔离**：
    - 学生端：通过user_id限制操作。
    - 辅导员端：页面只显示本班数据（通过`/api/counselor/*`接口）。
    - 管理员端：页面只显示本校数据（通过`/api/admin/*`接口）。
  - **统计图表**：
    - `StatChart.vue`使用ECharts渲染饼图（地区、薪资、企业性质）和折线图（就业趋势）。
  - **其他**：
    - 使用Element Plus的Form组件实现登记和设置页面，Table组件展示数据。
    - Pinia管理用户状态（user_id、school_id、role）。
    - Vue Router实现角色动态路由。
- **后端（Spring Boot+MyBatis Plus）**：
  - **数据隔离**：
    - 辅导员接口通过counselor_id关联students表，过滤本班数据。
    - 管理员接口通过school_id过滤本校数据。
  - **业务逻辑**：
    - 反馈提交检查employment_status为employed。
    - 统计接口使用SQL聚合查询（GROUP BY region/salary/company_nature）。
  - **其他**：
    - MyBatis Plus自动生成CRUD，XML文件定义统计查询。
    - 配置CORS支持前端跨域请求。

### 7. 测试
- **单元测试**：
  - 后端：JUnit测试Service层（就业状态检查、数据隔离、统计逻辑）。
  - 前端：Vitest测试组件（ValidCode.vue验证、StatChart.vue渲染）。
- **集成测试**：
  - 测试登录（验证码显示与验证）、就业信息登记（含地区）、反馈提交（需已就业）、审核流程。
  - 测试数据隔离：辅导员只能访问本班，管理员只能访问本校。
  - 测试统计图表数据准确性（饼图、折线图）。
- **用户验收测试**：
  - 模拟学生（登记、反馈）、辅导员（审核、本班统计）、管理员（查询、本校统计、管理）操作。
  - 测试验证码开关（require_captcha）动态控制。

### 8. 文档与答辩准备
- **文档**：
  - 系统设计文档：描述架构、技术选型、数据库设计、接口设计、数据隔离、验证码机制。
  - 用户手册：说明各角色操作步骤，强调验证码前端验证和数据隔离。
  - 开发日志：记录开发问题和解决方案（如validcode集成、数据隔离实现）。
- **答辩准备**：
  - 演示学生（登记、反馈）、辅导员（审核、本班统计）、管理员（查询、本校统计、管理）功能。
  - 突出`ValidCode.vue`（纯前端验证）、数据隔离（SQL过滤）、统计图表（ECharts）。
  - 准备PPT，重点展示功能、数据库设计、接口实现、验证码和隔离机制。
  - 准备常见问题回答，如为何密码明文存储（简化设计）、validcode选择理由、数据隔离实现。

## 二、注意事项
- **验证码**：
  - `ValidCode.vue`需确保`validcode`组件易用，支持图片或滑动验证。
  - 前端通过`/api/settings`动态控制验证码显示。
- **安全性**：
  - 密码明文存储，答辩中说明为简化设计，生产需加密。
  - 简单认证机制，需告知评审存在安全风险。
- **数据隔离**：
  - SQL查询加入school_id和counselor_id过滤，防止越权。
  - 前端页面无其他学校/班级选择控件。
- **性能优化**：
  - 数据库索引：school_id、counselor_id、student_id、region。
  - 前end使用懒加载优化页面加载。
- **扩展性**：
  - 数据库支持多学校（school_id）。
  - API支持版本化，便于功能扩展。