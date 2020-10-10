<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> APK版本
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-table
                    :data="tableData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
            <!--<el-table-column type="selection" width="55" align="center"></el-table-column>-->
                <el-table-column prop="versionId" label="ID" width="55" align="center"></el-table-column>
                <el-table-column prop="versionName" label="版本名称"></el-table-column>
                <el-table-column prop="versionNum" label="版本号"></el-table-column>
                <el-table-column prop="versionDes"  label="版本描述"></el-table-column>
                <el-table-column prop="isForcedUpgrade"  label="是否强制升级"></el-table-column>

                <el-table-column width="100" label="是否强制升级">
                    <template slot-scope="scope">
                        <el-tag effect="dark" v-if="scope.row.isForcedUpgrade=='0'" type="danger">否</el-tag>
                        <el-tag effect="dark" v-else-if="scope.row.isForcedUpgrade=='1'" type="warning">是</el-tag>
                        <el-tag effect="dark" v-else type="warning">不存在类型</el-tag>
                    </template>
                </el-table-column>

                <el-table-column label="操作" width="280" align="center">
                    <template slot-scope="scope">
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                @click="handleEdit(scope.$index, scope.row)"
                                v-if="hasPerm('apk:update')"
                        >更新</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                        background
                        layout="total, prev, pager, next"
                        :current-page="query.pageIndex"
                        :page-size="query.pageSize"
                        :total="totalRecords"
                        @current-change="handlePageChange"
                ></el-pagination>
            </div>
        </div>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
            <el-form ref="form" :model="form" label-width="70px">
                <el-form-item label="ID">
                    <el-input v-model="form.versionId" disabled></el-input>
                </el-form-item>
                <el-form-item label="版本名称">
                    <el-input v-model="form.versionName" placeholder="请输入版本名称"></el-input>
                </el-form-item>
                <el-form-item label="版本号">
                    <el-input v-model="form.versionNum" type="number" @keydown="handleInput2" placeholder="请输入版本号"></el-input>
                </el-form-item>
                <el-form-item label="版本描述">
                    <el-input v-model="form.versionDes" placeholder="请输入版本描述"></el-input>
                </el-form-item>
                <el-form-item label="是否强制升级">
                    <el-select v-model="form.isForcedUpgrade" placeholder="请选择">
                        <el-option key='0' label="否" value=0></el-option>
                        <el-option key='1' label="是" value=1></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="上传apk">
                    <el-upload
                            class="upload-demo"
                            drag
                            action="/api/file/apkUpload"
                            accept=".apk"
                            :on-success="handleAvatarSuccess"
                            prop="file"
                    >
                        <i class="el-icon-upload"></i>
                       <!-- <img v-if="form.apkPath" :src="form.apkPath" class="avatar">-->
                        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                        <div class="el-upload__tip" slot="tip">只能上传apk文件</div>
                    </el-upload>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import request from '../../utils/request';
    import { fetchPatientData } from '../../api/apkindex';
    export default {
        name: 'basetable',
        data() {
            return {
                query: {
                    phoneNum: '',
                    patientName: '',
                    userName: '',
                    pageIndex: 1,
                    pageSize: 10
                },
                tableData: [],
                multipleSelection: [],
                delList: [],
                editVisible: false,
                addVisible: false,
                passwordVisible: false,
                pdfVisible: false,
                expressVisible: false,
                updateExpressVisible: false,
                totalRecords: 0,
                form: {},
                idx: -1,
                id: -1,
                filePath: ""
            };
        },
        created() {
            this.getData();
        },
        methods: {
            // 获取 easy-mock 的模拟数据
            getData() {
                fetchPatientData(this.query).then(res => {
                    this.tableData = res.data;
                    this.totalRecords = 1;
                });
            },
            handleAvatarSuccess(response,file,fileList){
                this.form.apkPath = response.data;
            },
            handleInput2(e) {
                // 通过正则过滤小数点后两位
                e.target.value = (e.target.value.match(/^\d*(\.?\d{0,1})/g)[0]) || null
            },
            // 编辑操作
            handleEdit(index, row) {
                this.idx = index;
                this.form = row;
                this.editVisible = true;
                this.getData();
            },
            // 保存编辑
            saveEdit() {
                this.editVisible = false;
                const updateResult = query => {
                    query.foots = null;
                    return request({
                        url: '/api/apk/update',
                        method: 'post',
                        params: query,
                    });
                };
                this.form.versionId = null;
                updateResult(this.form).then(res => {
                    var code = res.code;
                    var errorMessage = res.errorMessage;
                    if(code == 200){
                        this.$message.success(`修改成功`);
                        this.$set(this.tableData, this.idx, this.form);
                    }else{
                        this.$message.error(errorMessage);
                    }
                });
            },
            // 分页导航
            handlePageChange(val) {
                this.$set(this.query, 'pageIndex', val);
                this.getData();
            }
        }
    };

</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }
    .table {
        width: 100%;
        font-size: 14px;
    }
    .red {
        color: #ff0000;
    }
    .mr10 {
        margin-right: 10px;
    }
    .table-td-thumb {
        display: block;
        margin: auto;
        width: 40px;
        height: 40px;
    }
</style>
