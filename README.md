# intelli-works-server
智能学工系统服务端

[TOC]

## 说明
### API说明

#### 验证老师邀请码

`GET` `/code/:code`

其中`:code`字段是实际待验证的邀请码。邀请码是基于计数的OTP。一共是44个字符，前12个对应教师UID，后32位代表使用AES加密的负载，正确解密后包含一个长整型数，表示该邀请码的序号。若序号大于服务器记载的序号，则表示验证成功，同时服务器记下该序号，该邀请码失效。

验证成功返回一个字符串，是APP的Token，app需要将其储存在本地，用以日后登陆。该Token会储存在服务器中以备查证。

验证失败返回字符串`"error"`。

#### 验证老师Token

`GET` `/code/verify/:token`

其中`:token`字段是实际待验证的token。服务器将会通过前12位字符判断老师，并获取数据库中存储的token与待验证的对比。

对比成功则返回：

```json
{
    "teacherName": "测试人",
    "unread": 0
}
```

老师姓名和当前未读消息计数。

对比失败则返回一个空的JSON实体：`{}`

#### 获取学生来访记录

`GET` `/fetch/record/:token`

获取来访记录需要使用Token进行验证，验证成功后才可获取访问记录的列表，验证失败返回空列表。

列表中每一个记录格式如下：

```json
{
    "sno": "18101130109",
    "name": "胡睿",
    "timestamp": 1567648019
}
```

`sno`为学生学号，`name`为姓名，`timestamp`为来访时间对应的Unix时间戳。

#### 获取学生头像

`GET` `/fetch/face/:sno`

`:sno`为学号。学号正确时从服务器返回学生的照片，学号不存在或学生没有照片则返回一张默认头像。返回的数据为`image/jpeg`格式。

#### 获取查询表格字段

`GET` `/fetch/:object`

`:object`可选字段：`grade`年级；`class`班级；`building`宿舍楼；`nation`民族；`sex`性别；`politic`政治面貌。请求其他字段会返回400错误。

返回格式为字符串列表。

#### 搜索学生

`POST` `/fetch/search`

POST请求内容：

```json
{
    "gender": "any",
    "politic": "any",
    "grade": "any",
    "cls": "any",
    "dorm": "any",
    "nationHelp": "any",
    "other": "",
    "token": ""
}
```

前7个字段对应App查询表单，token是上述接口中获取到的。

token验证通过将返回所查询学生的列表，失败或查不到学生则返回空列表。列表中的学生如下定义：

```json
{
    "grade": "2018",
    "cls": "计实验18",
    "sno": "18101130109",
    "name": "胡睿",
    "sex": "男",
    "area": "北京市",
    "nation": "满族",
    "quality": "非定向",
    "nation_help": "", 
    "gpa": "",
    "political": "",
    "phone": "15901441579",
    "parent_phone": "13700000000",
    "dorm": "7",
    "dorm_num": "303",
    "uid": "130823XXXXXXXXXXXX",
    "home": "北京市西城区XXX街XX号"
}
```

一目了然，不多说了。其中`nation_help`是国助生，`gpa`是绩点，`political`是政治面貌。上述三个字段在数据库中暂且为空。

#### 获取未读留言消息

`GET` `/fetch/message/:token`

服务器会自动通过token判断老师身份并返回最近30天的消息列表，列表按照时间降序排列，最新消息为首，个数最多为100条。

每一条消息如下定义：

```json
{
    "msgId": 1, 
    "id": 18101130109,
    "name": "胡睿",
    "message": "到此一游",
    "level": "important",
    "status": true,
    "timestamp": 1567648019
}
```

`msgId`为数据库中的消息id，全局唯一，用以更新消息阅读状态。`id`留言学生学号。`name`留言学生姓名。`message`留言内容。`level`留言紧急程度，可选值：`important`紧急，`normal`一般，`low`不重要。`status`留言阅读状态，为真时未读。`timestamp`留言时间对应的时间戳。

#### 更新消息阅读状态

`POST` `/update/message/:id/:status`

其中`:id`为上面的`msgid`，`:status`是新的状态，`0`表示`False`，`1`表示`True`。若想标记消息为已读，请写`status`为`0`。

POST请求体为token字符串。如无异常服务器返回字符串`"ok"`，若写入数据库异常，则返回`"error"`。

#### 创建深度辅导记录

`POST` `/notes/create`

POST请求体如下：

```json
{
    "sno": 18101130109, 
    "category": "其他问题", 
    "level": "一般", 
    "note": "辅导内容", 
    "comment": "常规处理", 
    "token": ""
}
```

`sno`为辅导学生学号，`category`为问题类别，`level`为严重程度，`comment`为后续处理意见，使用中文内容，所见即所得。`note`为辅导内容备注。`token`是上面获取到的。

#### 获取某学生的辅导历史

`GET` `notes/fetch/:token/:sno`

`:token`为验证用字段，`:sno`为学生学号。

验证成功后服务器将返回辅导记录列表，失败则返回空列表。

每条辅导记录如下定义：

```json
{
    "id": 1,
    "sno": "18101130109", 
    "studentName": "胡睿", 
    "teacherName": "测试人", 
    "category": "其他问题", 
    "level": "一般", 
    "note": "辅导内容", 
    "comment": "常规处理",
    "timestamp": 1567648019
}
```

`id`为辅导记录在数据库中的唯一编号，为后续管理接口之用，暂时没用。其他略。

#### 获取某老师的辅导历史

`GET` `/notes/history/:token`

服务器会通过token判断老师身份并返回对应的辅导记录列表，格式同上面一个接口。验证失败会返回空列表。

#### 通过学号获取学生信息

`POST` `/fetch/student/:sno`

`:sno`为学号。

POST请求体为token字符串。

成功则返回一个学生的详细信息（格式同上，参阅「搜索学生」一节），失败则返回一个空的JSON对象。

#### 学生端验证留言地址是否有效

`POST` `/token`

POST请求的内容就是Token本身，一共三步验证：判断长度为44字符，验证前12位教师UID是否有效，验证后32位负载是否过期。

上述环节其一不通过则返回失败：

```json
{
    "validate": false,
    "name": "",
    "ticket": ""
}
```

成功则返回老师的姓名和一个存在内存中、有效期为15分钟的Ticket：

```json
{
    "validate": false,
    "name": "测试人",
    "ticket": "MTIzNDU2Nzg5MDEyMzQ1Njc4"
}
```

其中ticket字段随机生成，生成时会记录其生成时间和相关老师，作为提交消息时的验证凭据。有效期15分钟

#### 提交消息

`POST` `/submit`

POST请求内容：

```json
{
    "id": 18101130109,
    "name": "胡睿";
    "ticket": "MTIzNDU2Nzg5MDEyMzQ1Njc4";
    "level": "normal",
    "message": "消息内容"
}
```

`id`是学生学号；`name`是姓名；`ticket`是从上面接口获取到的字段；`level`为消息重要程度，可取三个值：`low`不着急，`normal`一般，`important`重要；`message`是留言内容。

服务器会首先验证`ticket`是否有效，随后验证学号和姓名是否正确。验证通过后会尝试将消息写入数据库，如无异常则返回成功：

```json
{
    "status": "ok"
}
```

若写入数据库失败则返回：

```json
{
    "status": "failed"
}
```

学号和姓名对不上则返回：

```json
{
    "status": "404"
}
```

ticket过期则返回：

```json
{
    "status": "expired"
}
```



