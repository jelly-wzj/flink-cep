import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Home from '@/components/Home'
import ArticleList from '@/components/ArticleList'
import CateMana from '@/components/CateMana'
import DataCharts from '@/components/DataCharts'
import PostArticle from '@/components/PostArticle'
import UserMana from '@/components/UserMana'
import BlogDetail from '@/components/BlogDetail'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: '登录',
      hidden: true,
      component: Login
    }, {
      path: '/home',
      name: '',
      component: Home,
      hidden: true
    }, {
      path: '/home',
      component: Home,
      name: 'Overview',
      children: [
        {
          path: '/articleList',
          name: 'Overview',
          iconCls: 'fa fa-home fa-fw',
          component: ArticleList
        }
      ]
     }, {
      path: '/home',
      component: Home,
      name: '任务管理',
      iconCls: 'fa fa-file-text-o',
      children: [
        {
          path: '/articleList',
          name: '任务列表',
          component: ArticleList,
          meta: {
            keepAlive: true
          }
        }, {
          path: '/postArticle',
          name: '任务管理',
          component: PostArticle,
          meta: {
            keepAlive: false
          }
        }
      ]
    }, {
      path: '/home',
      component: Home,
      name: 'Console',
      children: [
        {
          path: '/cateMana',
          iconCls: 'fa fa-pencil fa-fw',
          name: 'Console',
          component: CateMana
        }
      ]
    },{
      path: '/home',
      component: Home,
      iconCls: 'fa fa-cog fa-fw',
      name: '系统设置',
      children: [
        {
          path: '/user',
          name: '用户',
          component: UserMana
        },
        {
           path: '/user',
           name: '日志',
           component: UserMana
       }
     ]
     }, {
      path: '/home',
      component: Home,
      name: '帮助文档',
      children: [
        {
          path: '/charts',
          iconCls: 'fa fa-book fa-fw',
          name: '帮助文档',
          component: DataCharts
        }
      ]
    }
  ]
})
