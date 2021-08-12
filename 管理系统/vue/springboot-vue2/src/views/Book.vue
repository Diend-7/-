<template>
  <div style="padding: 10px" >
    <div>
      <!--    功能区域-->
      <div >
        <el-button @click="add" type="primary" >新增</el-button>
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
        prop="name"
        label="名称"
      >
      </el-table-column>
      <el-table-column
        prop="price"
        label="价格">
      </el-table-column>
      <el-table-column
        prop="author"
        label="作者">
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="发布时间">
      </el-table-column>
      <el-table-column
        label="封面">
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.cover"
            :preview-src-list="[scope.row.cover]"></el-image>
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
            v-model="visible1">
            <p>这是一段内容这是一段内容确定删除吗？</p>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="visible1 = false">取消</el-button>
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
        <el-form-item  label="名称">
          <el-input v-model="form.name" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item  label="价格">
          <el-input v-model="form.price" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item  label="作者">
          <el-input v-model="form.author" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item  label="发布日期">
          <el-date-picker v-model="form.createTime" value-format="YYYY-MM-DD" style="width: 80%;" clearable></el-date-picker>
        </el-form-item>
        <el-form-item  label="封面">
          <el-upload ref="upload"
            action="http://localhost:9090/files/upload" :on-success="filesUploadSuccess">
            <el-button type="primary">点击上传</el-button>
          </el-upload>
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
    name: 'Book',
    data() {
      return {
        form:{},
        dialogVisible:false,
        handleClose: true,
        search:'',
        currentPage:1,
        pageSize:10,
        total:1,
        tableData: [],
        visible1:false
      }
    },
    created() {
      this.load();
    },
    methods:{
      filesUploadSuccess(response){
        console.log(response)
        this.form.cover=response.data;
        console.log(this.form);
      },
      load(){
        request.get("/book",{
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
        this.$refs['upload'].clearFiles();
      },
      save(){
        if (this.form.id) {  // 更新
          request.put("/book", this.form).then(res => {
            console.log(res)
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "更新成功"
              })
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
            this.load() // 刷新表格的数据
            this.dialogVisible = false  // 关闭弹窗
          })
        }  else {  // 新增
          request.post("/book", this.form).then(res => {
            console.log(res)
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "新增成功"
              })
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }

            this.load() // 刷新表格的数据
            this.dialogVisible = false  // 关闭弹窗
          })
        }

      },
      handleEdit(row){
        this.form=JSON.parse(JSON.stringify(row));
        this.dialogVisible=true;
        this.$nextTick(()=>{
          this.$refs['upload'].clearFiles();
        })

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
        request.delete("/book/"+id).then(res=>{
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
