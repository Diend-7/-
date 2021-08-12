import Vue from 'vue'
import Router from 'vue-router'
import Home from "../views/User";
import layout from "../layout/layout";
import Register from "../views/Register";
import User from "../views/User";
import Book from "../views/Book";
import Person from "../views/Person";
import News from "../views/News";



Vue.use(Router)
const routes = [
  { name: "Layout",
    path: '/',
    component: layout,
    redirect: "/user",
    children:[
      {
        name: "User",
        path: '/user',
        component: User,
      },
      {
        name: "Book",
        path: '/book',
        component: Book },
      {
        name: "Person",
        path: '/person',
        component: Person },
      {
        name: "News",
        path: '/news',
        component: News,
      },
    ]
  },
  { name: "Login",
    path: '/login',
    component: ()=>import("@/views/Login") },
  { name: "Register",
    path: '/register',
    component: Register },
  {
    name: "Person",
    path: '/person',
    component: Person }

]

export default new Router({
  routes
})
