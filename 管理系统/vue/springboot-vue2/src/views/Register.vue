<template>
  <div style="width:100%;height: 100vh;background-color: lightskyblue; overflow: hidden">
    <div style="width: 400px;margin: 200px auto ;">
      <div style="color:white;font-size: 30px;text-align: center">注册</div>
      <el-form ref="form" :model="form" size="normal" style="margin-top:20px" :rules="rules">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user-solid" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item  prop="password">
          <el-input prefix-icon="el-icon-lock" v-model="form.password" show-password></el-input>
        </el-form-item>
        <el-form-item prop="confirm">
          <el-input prefix-icon="el-icon-lock" v-model="form.confirm" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="register">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  import request from "../../utils/request";

  export default {
    name: "Register",
    data(){
      return{
        form:{},
        rules:{
          username: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
          ],
          confirm: [
            { required: true, message: '请输入密码', trigger: 'blur' },
          ],
        }
      }
    },
    methods:{
      register(){
        this.$refs['form'].validate((valid) => {
          if (valid) {
            if(this.form.password!==this.form.confirm){
              this.$message({
                type: "error",
                message: "两次密码不统一"
              })
            }
            request.post("/user/register",this.form).then(res=>{
              if (res.code === '0') {
                this.$message({
                  type: "success",
                  message: "注册成功"
                })
                sessionStorage.setItem("user", JSON.stringify(res.data))  // 缓存用户信息
                this.$router.push("/login")  //登录成功之后进行页面的跳转，跳转到主页
              } else {
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            })
          }
        })


      }

    }
  }
</script>

<style scoped>

</style>
