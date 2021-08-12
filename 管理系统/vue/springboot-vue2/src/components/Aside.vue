<template>
  <div>
    <el-menu
      style="width: 200px;height: calc(100vh - 70px);"
      :default-active="path"
      class="el-menu-vertical-demo"
      router
      >
      <el-submenu index="1" v-if="user.role===1">
        <template #title>系统管理</template>
        <el-menu-item index="/user"  >用户管理</el-menu-item>
      </el-submenu>
      <el-menu-item index="/book" >书籍管理</el-menu-item>
      <el-menu-item index="/news" >新闻管理</el-menu-item>
    </el-menu>
  </div>
</template>

<script>
  import request from "../../utils/request";

  export default {
    name: "Aside",
    data(){
      return{
        path:this.$route.path,
        user:{}
      }
    },
    created() {
      let str=sessionStorage.getItem("user")||{};
      this.user=JSON.parse(str);

      // 获取用户的信息
      request.get("/user/"+this.user.id).then(res=>{
        if(res.code===0){
          this.user=res.data;
        }
      })
      console.log(this.user)
    }
  }
</script>

<style scoped>

</style>
