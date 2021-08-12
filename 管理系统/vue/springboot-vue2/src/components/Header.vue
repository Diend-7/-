<template>
  <div style="height:50px; line-height:50px; border-bottom: 1px solid #cccccc;display: flex">
    <div style="width: 200px; padding-left: 30px; font-weight: bold;color: dodgerblue">管理系统</div>
    <div style="flex: 1"></div>
    <!--    下拉菜单开始-->
    <div style="width: 100px;padding-right: 30px">
      <el-dropdown router>
        <span class="el-dropdown-link">
          {{user.nickName}}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu >
          <el-button type="text" @click="showInfo">个人信息</el-button>
          <el-button type="text" @click="exit">退出体统</el-button>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <!--    下拉菜单结束-->
  </div>
</template>

<script>
  import request from "../../utils/request";

  export default {
    name: "Header",
    data() {
      return {
        user: {}
      }
    },
    created() {
      let str = sessionStorage.getItem("user") || "{}"
      this.user = JSON.parse(str)
    },
    methods: {
      exit(){
        console.log("1");
        this.$router.push("/login")
      },
      showInfo(){
        this.$router.push("/person")
      }
    }
  }
</script>

<style scoped>

</style>
