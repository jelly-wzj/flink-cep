/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.6.20 : Database - roc
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`roc` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `roc`;

/*Table structure for table `article` */

DROP TABLE IF EXISTS `article`;

CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `mdContent` text COMMENT 'md文件源码',
  `htmlContent` text COMMENT 'html源码',
  `summary` text,
  `cid` int(11) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `publishDate` datetime DEFAULT NULL,
  `editTime` datetime DEFAULT NULL,
  `state` int(11) DEFAULT NULL COMMENT '0表示草稿箱，1表示已发表，2表示已删除',
  `pageView` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `cid` (`cid`),
  KEY `uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8;

/*Data for the table `article` */

insert  into `article`(`id`,`title`,`mdContent`,`htmlContent`,`summary`,`cid`,`uid`,`publishDate`,`editTime`,`state`,`pageView`) values (108,'Linux中安装zookeeper','最近打算出一个系列，介绍Dubbo的使用。\n\n---\n分布式应用现在已经越来越广泛，Spring Could也是一个不错的一站式解决方案，不过据我了解国内目前貌似使用阿里Dubbo的公司比较多，一方面这个框架也确实很OK，另一方面可能也是因为Dubbo的中文文档比较全的缘故，据Dubbo官网上的消息，阿里已经重新开始了对Dubbo的维护，这也算是使用Dubbo的互联网公司的福音了吧。OK，废话不多说，今天我们就先来看看如何在Linux上安装zookeeper。\n\n---\n\n了解过Dubbo的小伙伴都知道，Dubbo官方建议我们使用的注册中心就是zookeeper，zookeeper本来是Hadoop的一个子项目，现在发展成了Apache的顶级项目，看名字就知道Zookeeper就是动物园管理员，管理Hadoop(大象)、Hive(蜂房/蜜蜂)等动物。Apache上的Zookeeper分Linux版和Windows版，但是考虑到实际生产环境都是Linux，所以我们这里主要介绍Linux上Zookeeper的安装，Windows上Zookeeper的安装则比较简单，下载解压即可，和Tomcat差不多。\n\nOK，废话不多说，接下来我们就来看看zookeeper的安装步骤。\n\n---\n环境：\n>1.VMware® Workstation 12 Pro  \n>2.CentOS7  \n>3.zookeeper-3.4.10(本文写作时的最新稳定版)  \n\n---\n# 安装步骤\n1.下载zookeeper\n\nzookeeper下载地址如下，小伙伴们可以在第一个地址中选择适合自己的zookeeper版本，也可以直接点击第二个地址下载我们本文使用的zookeeper。\n\n>1.[http://mirrors.hust.edu.cn/apache/zookeeper/](http://mirrors.hust.edu.cn/apache/zookeeper/)  \n\n>2.[http://mirrors.hust.edu.cn/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz](http://mirrors.hust.edu.cn/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz)\n\n\n\n2.将下载好的zookeeper上传到Linux服务器上\n\n上传方式多种多样，我这里采用了xftp，小伙伴们也可以直接使用putty上传，上传结果如下：\n![这里写图片描述](http://img.blog.csdn.net/20170825114622362?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\n\n3.将文件解压到/opt目录下  \n\n![这里写图片描述](http://img.blog.csdn.net/20170825115122378?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\n\n4.进入到刚刚解压好的目录中，创建两个文件夹，分别是data和logs，如下：\n\n![这里写图片描述](http://img.blog.csdn.net/20170825115324970?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\n\n5.将解压后zookeeper-3.4.10文件夹下的zoo_sample.cfg文件拷贝一份命名为zoo.cfg，如下：\n\n![这里写图片描述](http://img.blog.csdn.net/20170825115426251?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\n\n6.修改zoo.cfg文件，添加data和log目录，如下：\n\n![这里写图片描述](http://img.blog.csdn.net/20170825115527367?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\n\n>1.2888 端口号是zookeeper服务之间通信的端口   \n>2.3888 是zookeeper 与其他应用程序通信的端口  \n>3.initLimit：这个配置项是用来配置 Zookeeper 接受客户端（这里所说的客户端不是用户连接 Zookeeper服务器的客户端，而是 Zookeeper 服务器集群中连接到 Leader 的 Follower 服务器）初始化连接时最长能忍受多少个心跳时间间隔数。当已经超过 10 个心跳的时间（也就是 tickTime）长度后 Zookeeper 服务器还没有收到客户端的返回信息，那么表明这个客户端连接失败。总的时间长度就是 5*2000=10 秒。  \n>4.syncLimit：这个配置项标识 Leader 与 Follower 之间发送消息，请求和应答时间长度，最长不能超过多少个 tickTime 的时间长度，总的时间长度就是 2*2000=4 秒  \n>5.server.A=B:C:D：其中 A 是一个数字，表示这个是第几号服务器；B 是这个服务器的IP地址或/etc/hosts文件中映射了IP的主机名；C 表示的是这个服务器与集群中的 Leader 服务器交换信息的端口；D 表示的是万一集群中的 Leader 服务器挂了，需要一个端口来重新进行选举，选出一个新的 Leader，而这个端口就是用来执行选举时服务器相互通信的端口。如果是伪集群的配置方式，由于 B 都是一样，所以不同的 Zookeeper 实例通信端口号不能一样，所以要给它们分配不同的端口号。\n\n7.在 dataDir=/opt/zookeeper-3.4.10/data下创建 myid文件 编辑myid文件，并在对应的IP的机器上输入对应的编号。如在zookeeper上，myid文件内容就是1。如果只在单点上进行安装配置，那么只有一个server.1。如下：\n\n![这里写图片描述](http://img.blog.csdn.net/20170825115647920?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\n\n8.在.bash_profile文件中增加zookeeper配置：\n\n![这里写图片描述](http://img.blog.csdn.net/20170825115729473?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\n\n9.使配置生效\n\n![这里写图片描述](http://img.blog.csdn.net/20170825115807787?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\n\n10.关闭防火墙\n\n![这里写图片描述](http://img.blog.csdn.net/20170825115848488?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\n\n11.启动并测试\n\n![这里写图片描述](http://img.blog.csdn.net/20170825115938795?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\n\n>启动之后如果能看到Mode:standalone就表示启动成功了。\n\n12.关闭zookeeper\n\n![这里写图片描述](http://img.blog.csdn.net/20170825121021364?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\n\n13.配置开机启动zookeeper\n\n![这里写图片描述](http://img.blog.csdn.net/20170825121059827?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)\n\n>**注意注意** 在centos7中，/etc/rc.local的权限被降低了，所以需要执行如下命令赋予其可执行权限\n```chmod +x /etc/rc.d/rc.local```\n\n\nOK,以上就是我们在CentOS7中安装zookeeper的全过程，做好这一切之后我们就可以在Dubbo中使用这个注册中心了，这个我们放在下一篇博客中介绍。\n\n更多JavaEE资料请关注公众号：\n\n![](http://img.blog.csdn.net/20170823174820001?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)','<p>最近打算出一个系列，介绍Dubbo的使用。</p>\n<hr />\n<p>分布式应用现在已经越来越广泛，Spring Could也是一个不错的一站式解决方案，不过据我了解国内目前貌似使用阿里Dubbo的公司比较多，一方面这个框架也确实很OK，另一方面可能也是因为Dubbo的中文文档比较全的缘故，据Dubbo官网上的消息，阿里已经重新开始了对Dubbo的维护，这也算是使用Dubbo的互联网公司的福音了吧。OK，废话不多说，今天我们就先来看看如何在Linux上安装zookeeper。</p>\n<hr />\n<p>了解过Dubbo的小伙伴都知道，Dubbo官方建议我们使用的注册中心就是zookeeper，zookeeper本来是Hadoop的一个子项目，现在发展成了Apache的顶级项目，看名字就知道Zookeeper就是动物园管理员，管理Hadoop(大象)、Hive(蜂房/蜜蜂)等动物。Apache上的Zookeeper分Linux版和Windows版，但是考虑到实际生产环境都是Linux，所以我们这里主要介绍Linux上Zookeeper的安装，Windows上Zookeeper的安装则比较简单，下载解压即可，和Tomcat差不多。</p>\n<p>OK，废话不多说，接下来我们就来看看zookeeper的安装步骤。</p>\n<hr />\n<p>环境：</p>\n<blockquote>\n<p>1.VMware® Workstation 12 Pro<br />\n2.CentOS7<br />\n3.zookeeper-3.4.10(本文写作时的最新稳定版)</p>\n</blockquote>\n<hr />\n<h1>安装步骤</h1>\n<p>1.下载zookeeper</p>\n<p>zookeeper下载地址如下，小伙伴们可以在第一个地址中选择适合自己的zookeeper版本，也可以直接点击第二个地址下载我们本文使用的zookeeper。</p>\n<blockquote>\n<p>1.<a href=\"http://mirrors.hust.edu.cn/apache/zookeeper/\" target=\"_blank\">http://mirrors.hust.edu.cn/apache/zookeeper/</a></p>\n</blockquote>\n<blockquote>\n<p>2.<a href=\"http://mirrors.hust.edu.cn/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz\" target=\"_blank\">http://mirrors.hust.edu.cn/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz</a></p>\n</blockquote>\n<p>2.将下载好的zookeeper上传到Linux服务器上</p>\n<p>上传方式多种多样，我这里采用了xftp，小伙伴们也可以直接使用putty上传，上传结果如下：<br />\n<img src=\"http://img.blog.csdn.net/20170825114622362?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\" alt=\"这里写图片描述\" /></p>\n<p>3.将文件解压到/opt目录下</p>\n<p><img src=\"http://img.blog.csdn.net/20170825115122378?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\" alt=\"这里写图片描述\" /></p>\n<p>4.进入到刚刚解压好的目录中，创建两个文件夹，分别是data和logs，如下：</p>\n<p><img src=\"http://img.blog.csdn.net/20170825115324970?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\" alt=\"这里写图片描述\" /></p>\n<p>5.将解压后zookeeper-3.4.10文件夹下的zoo_sample.cfg文件拷贝一份命名为zoo.cfg，如下：</p>\n<p><img src=\"http://img.blog.csdn.net/20170825115426251?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\" alt=\"这里写图片描述\" /></p>\n<p>6.修改zoo.cfg文件，添加data和log目录，如下：</p>\n<p><img src=\"http://img.blog.csdn.net/20170825115527367?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\" alt=\"这里写图片描述\" /></p>\n<blockquote>\n<p>1.2888 端口号是zookeeper服务之间通信的端口<br />\n2.3888 是zookeeper 与其他应用程序通信的端口<br />\n3.initLimit：这个配置项是用来配置 Zookeeper 接受客户端（这里所说的客户端不是用户连接 Zookeeper服务器的客户端，而是 Zookeeper 服务器集群中连接到 Leader 的 Follower 服务器）初始化连接时最长能忍受多少个心跳时间间隔数。当已经超过 10 个心跳的时间（也就是 tickTime）长度后 Zookeeper 服务器还没有收到客户端的返回信息，那么表明这个客户端连接失败。总的时间长度就是 5<em>2000=10 秒。<br />\n4.syncLimit：这个配置项标识 Leader 与 Follower 之间发送消息，请求和应答时间长度，最长不能超过多少个 tickTime 的时间长度，总的时间长度就是 2</em>2000=4 秒<br />\n5.server.A=B:C:D：其中 A 是一个数字，表示这个是第几号服务器；B 是这个服务器的IP地址或/etc/hosts文件中映射了IP的主机名；C 表示的是这个服务器与集群中的 Leader 服务器交换信息的端口；D 表示的是万一集群中的 Leader 服务器挂了，需要一个端口来重新进行选举，选出一个新的 Leader，而这个端口就是用来执行选举时服务器相互通信的端口。如果是伪集群的配置方式，由于 B 都是一样，所以不同的 Zookeeper 实例通信端口号不能一样，所以要给它们分配不同的端口号。</p>\n</blockquote>\n<p>7.在 dataDir=/opt/zookeeper-3.4.10/data下创建 myid文件 编辑myid文件，并在对应的IP的机器上输入对应的编号。如在zookeeper上，myid文件内容就是1。如果只在单点上进行安装配置，那么只有一个server.1。如下：</p>\n<p><img src=\"http://img.blog.csdn.net/20170825115647920?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\" alt=\"这里写图片描述\" /></p>\n<p>8.在.bash_profile文件中增加zookeeper配置：</p>\n<p><img src=\"http://img.blog.csdn.net/20170825115729473?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\" alt=\"这里写图片描述\" /></p>\n<p>9.使配置生效</p>\n<p><img src=\"http://img.blog.csdn.net/20170825115807787?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\" alt=\"这里写图片描述\" /></p>\n<p>10.关闭防火墙</p>\n<p><img src=\"http://img.blog.csdn.net/20170825115848488?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\" alt=\"这里写图片描述\" /></p>\n<p>11.启动并测试</p>\n<p><img src=\"http://img.blog.csdn.net/20170825115938795?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\" alt=\"这里写图片描述\" /></p>\n<blockquote>\n<p>启动之后如果能看到Mode:standalone就表示启动成功了。</p>\n</blockquote>\n<p>12.关闭zookeeper</p>\n<p><img src=\"http://img.blog.csdn.net/20170825121021364?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\" alt=\"这里写图片描述\" /></p>\n<p>13.配置开机启动zookeeper</p>\n<p><img src=\"http://img.blog.csdn.net/20170825121059827?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\" alt=\"这里写图片描述\" /></p>\n<blockquote>\n<p><strong>注意注意</strong> 在centos7中，/etc/rc.local的权限被降低了，所以需要执行如下命令赋予其可执行权限<br />\n<code>chmod +x /etc/rc.d/rc.local</code></p>\n</blockquote>\n<p>OK,以上就是我们在CentOS7中安装zookeeper的全过程，做好这一切之后我们就可以在Dubbo中使用这个注册中心了，这个我们放在下一篇博客中介绍。</p>\n<p>更多JavaEE资料请关注公众号：</p>\n<p><img src=\"http://img.blog.csdn.net/20170823174820001?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjcwMjU0Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast\" alt=\"\" /></p>\n','最近打算出一个系列，介绍Dubbo的使用。\n\n分布式应用现在已经越来越广泛，Spring Could',60,6,'2017-12-24 10:05:20','2017-12-24 10:05:20',1,1);

/*Table structure for table `article_tags` */

DROP TABLE IF EXISTS `article_tags`;

CREATE TABLE `article_tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aid` int(11) DEFAULT NULL,
  `tid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tid` (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

/*Data for the table `article_tags` */

insert  into `article_tags`(`id`,`aid`,`tid`) values (26,116,42),(27,116,44),(28,116,35),(29,118,45),(32,119,40),(33,119,41),(36,109,35),(37,109,50),(38,109,51),(39,110,36),(48,108,33),(49,108,34),(50,120,66),(51,120,65);

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cateName` varchar(64) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;

/*Data for the table `category` */

insert  into `category`(`id`,`cateName`,`date`) values (56,'Vue22','2017-12-21'),(58,'人生感悟','2017-12-21'),(60,'JavaEE','2017-12-21'),(61,'Git','2017-12-21'),(62,'Linux','2017-12-21'),(64,'MongoDB','2017-12-23');

/*Table structure for table `comments` */

DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aid` int(11) DEFAULT NULL,
  `content` text,
  `publishDate` datetime DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL COMMENT '-1表示正常回复，其他值表示是评论的回复',
  `uid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `aid` (`aid`),
  KEY `uid` (`uid`),
  KEY `parentId` (`parentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `comments` */

/*Table structure for table `pv` */

DROP TABLE IF EXISTS `pv`;

CREATE TABLE `pv` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `countDate` date DEFAULT NULL,
  `pv` int(11) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pv_ibfk_1` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

/*Data for the table `pv` */

insert  into `pv`(`id`,`countDate`,`pv`,`uid`) values (1,'2017-12-24',20,6),(2,'2017-12-24',14,7),(4,'2017-12-25',40,6),(5,'2017-12-25',23,7),(6,'2017-12-26',11,6),(7,'2017-12-26',32,7),(26,'2017-12-23',2,6),(27,'2017-12-23',77,7);

/*Table structure for table `roles` */

DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `roles` */

insert  into `roles`(`id`,`name`) values (1,'超级管理员'),(2,'普通用户');

/*Table structure for table `roles_user` */

DROP TABLE IF EXISTS `roles_user`;

CREATE TABLE `roles_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rid` int(11) DEFAULT '2',
  `uid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rid` (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8;

/*Data for the table `roles_user` */

insert  into `roles_user`(`id`,`rid`,`uid`) values (1,1,1);

/*Table structure for table `tags` */

DROP TABLE IF EXISTS `tags`;

CREATE TABLE `tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tagName` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tagName` (`tagName`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;

/*Data for the table `tags` */

insert  into `tags`(`id`,`tagName`) values (66,'666'),(35,'Ajax'),(36,'Dubbo'),(40,'git'),(33,'Linux'),(45,'mongodb'),(42,'spring'),(44,'SpringSecurity'),(37,'websocket'),(34,'Zookeeper'),(50,'图片上传'),(51,'图片预览'),(41,'学习资料'),(65,'杂谈');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL,
  `nickname` varchar(64) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `email` varchar(64) DEFAULT NULL,
  `userface` varchar(255) DEFAULT NULL,
  `regTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`username`,`nickname`,`password`,`enabled`,`email`,`userface`,`regTime`) values (1,'admin','admin','21232f297a57a5a743894a0e4a801fc3',1,'roc@163.com','https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514093920321&di=913e88c23f382933ef430024afd9128a&imgtype=0&src=http%3A%2F%2Fp.3761.com%2Fpic%2F9771429316733.jpg','2017-12-21 13:30:29');

/*Table structure for table `pvview` */

DROP TABLE IF EXISTS `pvview`;

/*!50001 DROP VIEW IF EXISTS `pvview` */;
/*!50001 DROP TABLE IF EXISTS `pvview` */;

/*!50001 CREATE TABLE  `pvview`(
 `pv` decimal(32,0) ,
 `uid` int(11) 
)*/;

/*Table structure for table `totalpvview` */

DROP TABLE IF EXISTS `totalpvview`;

/*!50001 DROP VIEW IF EXISTS `totalpvview` */;
/*!50001 DROP TABLE IF EXISTS `totalpvview` */;

/*!50001 CREATE TABLE  `totalpvview`(
 `totalPv` decimal(32,0) ,
 `uid` int(11) 
)*/;

/*View structure for view pvview */

/*!50001 DROP TABLE IF EXISTS `pvview` */;
/*!50001 DROP VIEW IF EXISTS `pvview` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pvview` AS select sum(`pv`.`pv`) AS `pv`,`pv`.`uid` AS `uid` from `pv` group by `pv`.`uid` */;

/*View structure for view totalpvview */

/*!50001 DROP TABLE IF EXISTS `totalpvview` */;
/*!50001 DROP VIEW IF EXISTS `totalpvview` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `totalpvview` AS select sum(`a`.`pageView`) AS `totalPv`,`a`.`uid` AS `uid` from `article` `a` group by `a`.`uid` */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
