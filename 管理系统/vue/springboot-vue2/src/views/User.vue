<template>
  <div style="padding: 10px" >
    <div>
      <!--    功能区域-->
      <div style="margin: 10px;">
        <el-button type="primary" @click="add">新增</el-button>
        <el-button type="primary">导入</el-button>
        <el-button type="primary">导出</el-button>
      </div>
      <!--    搜索区域-->
      <div style="margin: 10px 0">
        <el-input

          placeholder="请输入关键字"
          v-model="search" style="width: 200px;"
        clearable>
        </el-input>
        <el-button type="primary" @click="load">查询</el-button>
      </div>
    </div>

    <el-table
      :data="tableData"
      border
      stripe
      style="width: 100%">
      <el-table-column
        prop="id"
        label="ID"
        sortable
        >
      </el-table-column>
      <el-table-column
        prop="username"
        label="用户名"
        >
      </el-table-column>
      <el-table-column
        prop="nickName"
        label="昵称">
      </el-table-column>
      <el-table-column
        prop="sex"
        label="性别">
      </el-table-column>
      <el-table-column
        prop="address"
        label="地址">
      </el-table-column>
      <el-table-column
      label="角色">
        <template slot-scope="scope">
          <span v-if="scope.row.role===1">管理员</span>
          <span v-if="scope.row.role===2">普通用户</span>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        >
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)"  >编辑</el-button>
          <el-popover
            placement="top"
            v-model="visible">
            <p>这是一段内容这是一段内容确定删除吗？</p>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="visible = false">取消</el-button>
              <el-button type="primary" size="mini" @click="handleDlete(scope.row.id)">确定</el-button>
            </div>
            <el-button slot="reference" type="danger" >删除</el-button>
          </el-popover>
        </template>
      </el-table-column>

    </el-table>
    <div style="margin: 10px 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[5,10,20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination >
    </div>
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="30%"
      >
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item  label="用户名">
          <el-input v-model="form.username" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item  label="昵称">
          <el-input v-model="form.nickName" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item  label="年龄">
          <el-input v-model="form.age" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item  label="性别">
          <el-radio v-model="form.sex" label="男">男</el-radio>
          <el-radio v-model="form.sex" label="女">女</el-radio>
          <el-radio v-model="form.sex" label="未知">未知</el-radio>

        </el-form-item>
        <el-form-item  label="地址">
          <el-input v-model="form.address" type="textarea" style="width: 80%"></el-input>
        </el-form-item>

      </el-form>

      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
  import request from "../../utils/request";

  export default {
    name: 'Home',
    data() {

      return {
        form:{},
        dialogVisible:false,
        handleClose: true,
        search:'',
        currentPage:1,
        pageSize:10,
        total:1,
        tableData: []
      }
    },
    created() {
      this.load();
    },
    methods:{
      load(){

        request.get("/user",{
          params:{
            pageNum: this.currentPage,
            pageSize:this.pageSize,
            search: this.search
          }

        }).then(res =>{
          console.log(res);
          this.tableData=res.data.records;
          this.total=res.data.total;
        })
      },
      add(){
        this.dialogVisible=true;
        this.form={};
      },
      save(){
        if(this.form.id){//编辑
          request.put("/user",this.form).then(res => {
            console.log(res);
            if(res.code==0){
              this.$message({
                type:"success",
                message:"编辑成功",
              })
            }else{
              this.$message({
                type: "error",
                message:"编辑失败",
              })
            }
          });
        }else{//新增
          request.post("/user",this.form).then(res => {
            console.log(res);
            if(res.code==0){
              this.$message({
                type:"success",
                message:"新增成功",
              })
            }else{
              this.$message({
                type: "error",
                message:"新增失败",
              })
            }

          });
        }

        this.dialogVisible=false;
        this.load();

      },
      handleEdit(row){
          this.form=JSON.parse(JSON.stringify(row));
          this.dialogVisible=true;
      },
      handleSizeChange(pageSize){//改变当前每页的个数触发
        this.pageSize=pageSize;
          this.load()
      },
      handleCurrentChange(pageNum){//改变当前页码触发
        this.currentPage=pageNum;
          this.load()
      },
      handleDlete(id){
        console.log(id);
        request.delete("/user/"+id).then(res=>{
          if(res.code==0){
            this.$message({
              type:"success",
              message:"删除成功",
            })
          }else{
            this.$message({
              type: "error",
              message:"删除失败",
            })
          }
          this.load()
        })

      },


    }
  }
</script>
