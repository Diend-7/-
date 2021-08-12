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
        prop="title"
        label="标题"
      >
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="content"-->
<!--        label="内容">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="author"
        label="作者">
      </el-table-column>
      <el-table-column
        prop="time"
        label="发布时间">
      </el-table-column>

      <el-table-column
        fixed="right"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button @click="details(scope.row)"  >详情</el-button>
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
      width="50%"
    >
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item  label="标题">
          <el-input v-model="form.title" style="width: 50%"></el-input>
        </el-form-item>
        <div id="div1">

        </div>
<!--        <el-form-item  label="内容">-->
<!--          <el-input v-model="form.content" style="width: 80%"></el-input>-->
<!--        </el-form-item>-->

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog :visible.sync="vis" title="详情">
      <el-card>
        <div v-html="detail.content" style="min-height: 100px"></div>
      </el-card>
    </el-dialog>
  </div>
</template>

<script>
  import request from "../../utils/request";
  import E from 'wangeditor'

  let editor;
  export default {
    name: 'News',
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
        detail:{},
        visible1:false,
        vis:false,
      }
    },
    created() {
      this.load();
    },
    mounted() {

    },
    methods:{

      details(row){
        this.detail=row;
        this.vis=true;
      },
      filesUploadSuccess(response){
        console.log(response)
        this.form.cover=response.data;
        console.log(this.form);
      },
      load(){
        request.get("/news",{
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
        if(this.$refs['upload']){
          this.$refs['upload'].clearFiles();
        }


        //关联弹窗里面的div1，new一个文本编辑器
        this.$nextTick(()=>{
          editor = new E('#div1')
          editor.config.uploadImgServer="http://localhost:9090/files/editor/upload"
          editor.config.uploadFileName="file"
          editor.create()


        })

      },
      save(){
        this.form.content=editor.txt.html();//获取输入的文本内容

        if (this.form.id) {  // 更新
          request.put("/news", this.form).then(res => {
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
          let userStr=sessionStorage.getItem("user")||{};
          let user=JSON.parse(userStr);
          this.form.author=user.nickName;
          request.post("/news", this.form).then(res => {
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
          editor = new E('#div1')
          editor.create()
          editor.txt.html(row.content)
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
        request.delete("/news/"+id).then(res=>{
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
