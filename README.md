**驰骋工作流程引擎资源 **
1. 在线文档: http://ccbpm.mydoc.io
2. 视频/资料教程下载: svn http://140.143.236.168:7080/svn/ccbpmdocs 用户名: ccbpm密码:ccbpm  也可以使用在线浏览.
3. 官方网站: http://ccflow.org 在线演示: http://demo.ccflow.org

#JFlow开源工作流

目前支持以下数据库(都能正确获取注释信息):

1. `Oracle`
2. `SqlServer`
3. `MySql`

流程引擎设计支持所见即所得的设计：节点设计、表单设计、单据设计、报表定义设计、以及用户菜单设计。

流程模式简洁，只有4种容易理解：线性流程、同表单分合流、异表单分合流、父子流程，没有复杂的概念。

配置参数丰富，支持流程的基础功能：前进、后退、转向、转发、撤销、抄送、挂起、草稿、任务池共享，也支持高级功能取回审批、项目组、外部用户等等。


## 快速运行体验

1. 创建一个新库，设置数据库参数：/jflow-web/src/main/resources/jflow.properties

2. 运行 bin/run-tomcat7.bat 文件，快速启动服务。

3. 第一次运行相对比较慢，请等待一会，因为需要下载jar类库，等待时间和本机网络速度有关。

4. 如果你想减少等待时间，jflow已自带repository，你只需解压 bin 文件夹下的 win_bin.part1.rar 文件包即可获得。

5. 如果你本机没有安装maven和jdk，你可以使用jflow自带的，也可以解压 bin 文件夹下的 win_bin.part1.rar 文件包即可获得。
	
6. 如果你是用SVN检出的项目，bat文件会丢失换行符，还请解压 win_bin.part1.rar 文件覆盖当前文件，即可。 

7. 启动成功后，即可通过浏览器进行访问：

	访问地址：http://127.0.0.1:8080/jflow-web/
	
	管理员账号：admin  密码：123 其他用户密码：123


## 嵌入式集成方法：


整合要求，基于Maven项目管理，Spring MVC 4.1 以上，Apache CXF 3.1 以上。

1、 pom.xml中添加如下依赖：

```
<dependency>
    <groupId>jflow-core</groupId>
    <artifactId>jflow-core</artifactId>
    <version>1.1.0-SNAPSHOT</version>
</dependency>
```

若有jar包冲突，请自行解决冲突。

2、 拷贝文件：

拷贝 jflow-web 项目下的 WF 和 DataUser 文件夹，到你的项目发布目录下。

拷贝 jflow-web 项目下的 jflow.properties、spring-context-jflow.xml和spring-mvc-jflow.xml文件，到你的项目的资源根目录下。

3、 在您的 spring context 配置文件中加入：

```
<import resource="classpath*:/spring-context-jflow.xml"/>
```

打开这个文件，修改JFlow使用的数据源，为您的数据源名称：
```
<property name="dataSource" ref="dataSource" /> 
```
集成您的登录登出（JFlow在获取当前登录信息的时候自动从该Key中获取用户信息，不使用时请注释掉此句）：
```
<property name="userNoSessionKey" value="你的当前用户登录的UserNo的SessionKey名称" />
```

4、 在您的spring mvc 配置文件中加入：

```
<import resource="classpath*:/spring-mvc-jflow.xml"/>
```

5、 在您的 web.xml 配置文件中加入：

```
<!-- Request Context Filter-->
<filter>
	<filter-name>requestContextFilter</filter-name>
	<filter-class>org.springframework.web.filter.RequestContextFilter</filter-class>
</filter>
<filter-mapping>
	<filter-name>requestContextFilter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>

<!-- Apache CXF Servlet -->
<servlet>
	<servlet-name>CXFServlet</servlet-name>
	<servlet-class>org.apache.cxf.transport.servlet.CXFServlet</servlet-class>
</servlet>
<servlet-mapping>
	<servlet-name>CXFServlet</servlet-name>
	<url-pattern>/service/*</url-pattern>
</servlet-mapping>
```

6、 组织机构权限整合

将您的数据库与JFLow数据库进行合并到同一数据库用户下，然后建立如下表视图表：

```
-- 部门
SELECT No,Name,ParentNo,Idx,NameOfPath,TreeNo,Leader,Tel,IsDir FROM PORT_Dept;

-- 岗位
SELECT No,Name,FK_StationType,DutyReq,Makings,StaGrade FROM PORT_Station;

-- 岗位类型
SELECT No,Name FROM PORT_StationType;

-- 用户
SELECT No,Name,Pass,FK_Dept,SID,FK_Duty,Leader,EmpNo,Tel,Email,NumOfDept,Idx FROM PORT_EMP;

-- 用户岗位
SELECT FK_Emp,FK_Station FROM PORT_EmpStation;
```
	
7、 整合UI统一风格：

调用Dev2Interface接口，实现待办、在途、抄送、流程查询等页面。具体请参考流程属性里的API及文档。

8、 webservice说明：

jflow提供Dev2Interface接口的webservice,包路径webcn.jflow.ws.JflowWSImpl.java。路径：http://localhost:8080/jflow-web/service/JflowWS?wsdl。
请结合”7、整合UI统一风格“使用。

#JFlow开源中国流介绍


驰骋工作流引擎研发与2003年，具有.net与java两个版本，这两个版本代码结构，数据库结构，设计思想，功能组成， 操作手册，完全相同。 导入导出的流程模版，表单模版两个版本完全通用。

CCFlow是.net版本的简称，由济南驰骋团队负责研发，JFlow是java版本的简称，在CCFlow的基础上升级改造而来，本公司联合易科德软件共同研发。两款产品向社会100%开源，十多年来，我们一直践行自己的诺言，真心服务中国IT产业，努力提高产品质量，成为了国内知名的老牌工作流引擎。

驰骋工作流引擎操作简单、概念通俗易懂、操作手册完善（计:14万操作手册说明书）、代码注释完整、案例丰富翔实、单元测试完整。

驰骋工作流引擎包含表单引擎与流程引擎两大部分，并且两块完美结合，协同高效工作.

流程与表单界面可视化的设计，可配置程度高，适应于中国国情的多种场景的需要。

驰骋工作流引擎发展与2003年，历经十多年的发展，在国内拥有最广泛的研究群体与应用客户群，是大型集团企业IT部门、软件公司、研究院、高校研究与应用的产品。

济南驰骋信息技术公司是国内为数不多的开源软件并且持续盈利的公司之一，在工作流程引擎领域是国内唯一盈利的开源软件。

驰骋工作流引擎不仅仅能够满足中小企业的需要，也能满足通信级用户的应用，先后在西门子、海南航空、中船、陕汽重卡、山东省国土资源厅、华电国际、江苏测绘院、厦门证券、天津港等国内外大型企业政府单位服役。

CCFlow拥有完整的解决方案:CCForm表单引擎、CCGPM权限管理系统、CCSSO单点登录系统、CCOA驰骋办公、CCIM即时通讯(能够满足20万人同时在线)。以上的解决方案除CCIM以外都是开源的一体化的解决方案。

JFlow 方便与您的开发框架嵌入式集成，与第三方组织机构视图化集成, 既有配置类型的开发适用于业务人员，IT维护人员， 也有面向程序员的高级引擎API开发。

该项目文档非常之详细

（1）流程设计器帮助：http://ccbpm.mydoc.io/

（2）表单设计器帮助：http://ccform.mydoc.io/

济南驰骋团队&山东易科德团队