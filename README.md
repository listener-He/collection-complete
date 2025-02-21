# collection-complete

## 项目简介

`collection-complete` 是一个用于处理集合数据并补充相关信息的Java库。它提供了链式调用的功能，可以方便地对集合中的元素进行批量操作和属性补充。

## 使用方式

### 前置条件
- Java 环境已安装 (版本 11 或以上)
- Maven 已安装

### 安装依赖

在项目根目录下运行以下命令来安装所有依赖：

### 运行项目

确保你已经在项目根目录下，并且已经安装了所有依赖。然后根据需要选择以下命令之一：

#### 启动应用

### 示例代码

以下是一个简单的示例，展示如何使用 `Complete` 类来补充用户信息：

```java
import cn.hehouhui.funcation.complete.Complete;
  public class Main { public static void main(String[] args) {
      // 场景1. 假设有一个用户列表 List<User> userList = getUsers();
      Complete.start(userList)
              // 补充用户名称
              .build(userService::getUsernameMap)
              .filter(user -> user.getUserId() > 0)
              .add(User::getUserId, User::setUsername)
              .then().over();
      // 场景2. 假设有一个用户列表 List<User> userList = getUsers();
      Complete.start(userList)
              // 补充用户名称
              .build(userService::getUsernameMap)
              .filter(user -> user.getUserId() > 0)
              .add(User::getUserId, User::setUsername)
              .bulid(userService::getOrderMap)
              .add(User::getUserId, (user, order) -> {
                  user.setOrderId(order.getOrderId());
                  user.setOrderNo(order.getOrderNo());
                  user.setStoreId(order.getStoreId());
                  user.setOrderTime(order.getCreateTime());
              })
              // 当依赖与其他的依赖关系时，可以添加doThen()方法 doThen()方法会先结束一次循环补充并清空补充函数
              .doThen()
              .build(storeService::getStoreNameMap)
              .add(User::getStoreId, User::setStoreName)
              .then().over();
            
    }
}
```

## 相关类介绍
**collection-complete** 项目主要由以下几个核心类组成，它们通过链式调用的方式协同工作，完成对集合数据的处理和补充：
1. Complete<E>:
这是整个流程的入口类，负责启动和管理整个补充流程。
通过 start(Collection<E> collection) 方法初始化一个 Complete 对象。
提供了 build 方法用于创建 Prepare 对象，并将其添加到执行器中。
通过 run() 和 finish(Executor executor) 方法执行流程，最终调用 over() 方法完成所有操作。
2. Prepare<I, N, E>:
负责准备和执行具体的补充操作。
通过 add 和 addColl 方法添加 SetGet 对象，用于定义如何获取和设置数据。
init(E target) 方法用于初始化准备工作，finish() 方法用于执行最终的补充操作。
3. SetGet<E, I, N>:
封装了从对象中获取 ID 和设置名称的逻辑。
通过 get(E target) 方法获取对象的 ID，通过 set(E target, N value) 方法设置对象的名称。
4. Write<I, N>:
负责管理 ID 集合和名称映射。
通过 add(I id) 方法添加 ID，通过 get() 方法获取名称映射。

```text

+-------------------+       +-------------------+       +-------------------+       +-------------------+
|   Complete<E>     |       |   Prepare<I,N,E>  |       |   SetGet<E,I,N>   |       |   Write<I,N>      |
|-------------------|       |-------------------|       |-------------------|       |-------------------|
| + start()         |<------| + add()           |<------| + get()           |       | + add()           |
| + build()         |       | + addColl()       |       | + set()           |       | + get()           |
| + run()           |       | + init()          |       +-------------------+       +-------------------+
| + finish()        |       | + finish()        |
| + over()          |       +-------------------+
+-------------------+

```

## 调用流程图
```text
+-------------------+       +-------------------+       +-------------------+       +-------------------+
|   Complete<E>     |       |   Prepare<I,N,E>  |       |   SetGet<E,I,N>   |       |   Write<I,N>      |
|-------------------|       |-------------------|       |-------------------|       |-------------------|
| + start()         |       | + add()           |       | + get()           |       | + add()           |
|                   |       | + addColl()       |       | + set()           |       | + get()           |
| + build()         |------>|                   |       |                   |       |                   |
|                   |       | + init()          |<------|                   |       |                   |
| + run()           |       | + finish()        |       |                   |       |                   |
| + finish()        |       |                   |       |                   |       |                   |
| + over()          |------>|                   |       |                   |       |                   |
+-------------------+       +-------------------+       +-------------------+       +-------------------+
```

**详细调用流程说明**
  1. 启动流程:
    通过 Complete.start(Collection<E> collection) 方法初始化一个 Complete 对象。
  2. 构建准备阶段:
    使用 Complete.build() 方法创建一个 Prepare 对象，并将其添加到执行器中。
  在 Prepare 对象中，通过 add() 或 addColl() 方法添加 SetGet 对象，定义如何获取和设置数据。
  3. 初始化准备:
    调用 Prepare.init(E target) 方法，对集合中的每个元素进行初始化操作，将 ID 添加到 Write 对象中。
  4. 执行补充操作:
    调用 Prepare.finish() 方法，获取名称映射，并对集合中的每个元素执行补充操作。
  5. 完成流程:
    调用 Complete.over() 方法，清空执行器，完成整个流程。

## 贡献者

- **HeHui**
    - 邮箱: hehouhui@foxmail.com
    - 个人主页: [https://www.hehouhui.cn](https://www.hehouhui.cn)
    - 博客网站: [https://blog.hehouhui.cn](https://blog.hehouhui.cn)

---

希望这份 `README.md` 文件能帮助您更好地理解和使用 `collection-complete` 项目。如果有任何问题或建议，请随时联系贡献者。
