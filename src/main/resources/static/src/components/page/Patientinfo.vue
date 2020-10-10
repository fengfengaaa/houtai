<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 患者信息
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button
                    type="primary"
                    icon="el-icon-delete"
                    class="handle-del mr10"
                    @click="delAllSelection"
                    v-if="hasPerm('patientinfo:batchdelete')"
                >批量删除</el-button>
                <el-input v-model="query.patientName" placeholder="患者姓名" class="handle-input mr10"></el-input>
                <el-input v-model="query.phoneNum" placeholder="联系方式" class="handle-input mr10"></el-input>
                <el-input v-model="query.userName" placeholder="创建用户" class="handle-input mr10"></el-input>
                <el-select v-model="query.makeStatu" placeholder="制作状态" class="handle-select mr10">
                    <el-option  label="请选择状态" :value=-1 ></el-option>
                    <el-option  label="不制作" :value=0 ></el-option>
                    <el-option  label="待制作" :value=1 ></el-option>
                    <el-option  label="制作中" :value=2 ></el-option>
                    <el-option  label="制作完成" :value=3 ></el-option>
                    <el-option  label="邮递中" :value=4 ></el-option>
                    <el-option  label="已签收" :value=5 ></el-option>
                </el-select>

                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                <el-button type="primary" icon="el-icon-search" @click="handleExportSearch">导出</el-button>
            </div>
            <el-table
                :data="tableData"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
                @selection-change="handleSelectionChange"
                @sort-change="changeTableSort"
            >
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="patientId" label="ID" width="55" align="center"></el-table-column>
                <el-table-column prop="patientName" width="80" label="姓名"></el-table-column>
                    <el-table-column width="65" label="性别">
                    <template slot-scope="scope">
                        <el-tag effect="dark" v-if="scope.row.sex=='1'" type="success">男</el-tag>
                        <el-tag effect="dark" v-else type="warning">女</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="phoneNum" width="120" label="联系方式">
                </el-table-column>
                <el-table-column prop="birthday" label="出生日期"></el-table-column>
                <el-table-column prop="height"  label="身高(CM)" ></el-table-column>
                <el-table-column prop="weight"  label="体重(KG)" ></el-table-column>
                <el-table-column prop="address" label="地址"></el-table-column>
               <!-- <el-table-column prop="filePath" label="地址"></el-table-column>-->
                <el-table-column prop="userName" label="创建用户"></el-table-column>
                <el-table-column prop="createDate" label="评估时间" :sortable="'custom'"></el-table-column>
                <el-table-column width="80" label="状态">
                    <template slot-scope="scope">
                        <el-tag effect="dark" v-if="scope.row.makeStatus=='0'" type="danger">不制作</el-tag>
                        <el-tag effect="dark" v-else-if="scope.row.makeStatus=='1'" type="warning">待制作</el-tag>
                        <el-tag effect="dark" v-else-if="scope.row.makeStatus=='2'" type="info">制作中</el-tag>
                        <el-tag effect="dark" v-else-if="scope.row.makeStatus=='3'" type="success">制作完成</el-tag>
                        <el-tag effect="dark" v-else-if="scope.row.makeStatus=='4'" type="success">邮寄中</el-tag>
                        <el-tag effect="dark" v-else type="warning">已签收</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="makePurpose" width="100" label="制作用途"></el-table-column>
                <el-table-column label="操作" width="280" align="center">
                    <template slot-scope="scope">
                        <el-button
                            type="text"
                            icon="el-icon-edit"
                            @click="handleEdit(scope.$index, scope.row)"
                            v-if="hasPerm('patientinfo:edit')"
                        >编辑</el-button>
                        <el-button
                            type="text"
                            icon="el-icon-delete"
                            class="red"
                            @click="handleDelete(scope.$index, scope.row)"
                            v-if="hasPerm('patientinfo:delete')"
                        >删除</el-button>
                      <!--  <el-button
                                type="text"
                                icon="el-icon-edit"
                                v-if="scope.row.makeStatus=='0' && hasPerm('patientinfo:toCreate')"
                                @click="handleStatus(scope.$index, scope.row)">
                            去制作
                        </el-button>-->
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                v-if="scope.row.makeStatus=='1' && hasPerm('patientinfo:startCreate')"
                                @click="handleStatus(scope.$index, scope.row)">
                            开始制作
                        </el-button>
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                v-if="scope.row.makeStatus=='2' && hasPerm('patientinfo:createFinish')"
                                @click="handleStatus(scope.$index, scope.row)">
                            制作完成
                        </el-button>
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                v-if="scope.row.makeStatus=='3' && hasPerm('patientinfo:mail')"
                                @click="handleStatus(scope.$index, scope.row)">
                            邮寄
                        </el-button>
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                v-if="scope.row.makeStatus=='4' && hasPerm('patientinfo:confirm')"
                                @click="handleStatus(scope.$index, scope.row)">
                            确认收货
                        </el-button>
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                v-if="scope.row.makeStatus=='5' && hasPerm('patientinfo:seeExpress')"
                                @click="showExpress(scope.$index, scope.row)">
                            查看快递
                        </el-button>
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                v-if="scope.row.makeStatus=='4' && hasPerm('patientinfo:editExpress')"
                                @click="showExpress(scope.$index, scope.row)">
                            修改快递
                        </el-button>
                        <el-button
                                type="text"
                                icon="el-icon-info"
                                v-if="hasPerm('patientinfo:seeDetail') || hasTurePerm()"
                                @click="handledetail(scope.row.filePath)"
                        >查看结果</el-button>
                        <el-button
                                type="text"
                                icon="el-icon-info"
                                v-if="hasPerm('patientinfo:seeReport')"
                                @click="handleReport(scope.row.reportPath)"
                        >查看报告</el-button>
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

            <div v-show="false">
                <el-table
                        :data="exportTableData"
                        border
                        class="table"
                        ref="multipleTable"
                        header-cell-class-name="table-header"
                        id="out-table"
                >
                    <el-table-column prop="patientId" label="ID" width="55" align="center"></el-table-column>
                    <el-table-column prop="patientName" width="80" label="姓名"></el-table-column>
                    <el-table-column width="65" label="性别">
                        <template slot-scope="scope">
                            <el-tag effect="dark" v-if="scope.row.sex=='1'" type="success">男</el-tag>
                            <el-tag effect="dark" v-else type="warning">女</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="phoneNum" width="120" label="联系方式">
                    </el-table-column>
                    <el-table-column prop="birthday" label="出生日期" ></el-table-column>
                    <el-table-column prop="height"  label="身高(CM)"></el-table-column>
                    <el-table-column prop="weight"  label="体重(KG)"></el-table-column>
                    <el-table-column prop="address" label="地址" ></el-table-column>
                    <!-- <el-table-column prop="filePath" label="地址"></el-table-column>-->
                    <el-table-column prop="userName" label="创建用户"></el-table-column>
                    <el-table-column prop="createDate" label="评估时间"></el-table-column>
                    <el-table-column width="80" label="状态">
                        <template slot-scope="scope">
                            <el-tag effect="dark" v-if="scope.row.makeStatus=='0'" type="danger">不制作</el-tag>
                            <el-tag effect="dark" v-else-if="scope.row.makeStatus=='1'" type="warning">待制作</el-tag>
                            <el-tag effect="dark" v-else-if="scope.row.makeStatus=='2'" type="info">制作中</el-tag>
                            <el-tag effect="dark" v-else-if="scope.row.makeStatus=='3'" type="success">制作完成</el-tag>
                            <el-tag effect="dark" v-else-if="scope.row.makeStatus=='4'" type="success">邮寄中</el-tag>
                            <el-tag effect="dark" v-else type="warning">已签收</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="makePurpose" width="100" label="制作用途"></el-table-column>
                </el-table>
                <div class="pagination">
                    <el-pagination
                            background
                            layout="total, prev, pager, next"
                            :current-page="1"
                            :page-size="1000000"
                            :total="exportTotalRecords"
                            @current-change="handlePageChange"
                    ></el-pagination>
                </div>
            </div>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
            <el-form ref="form" :model="form" label-width="70px">
                <el-form-item label="ID">
                    <el-input v-model="form.patientId" disabled></el-input>
                </el-form-item>
                <el-form-item label="姓名">
                    <el-input v-model="form.patientName" placeholder="请输入姓名" disabled></el-input>
                </el-form-item>
                <el-form-item label="性别">
                        <el-select v-model="form.sex" placeholder="请选择">
                            <el-option  label="男" :value=1></el-option>
                            <el-option  label="女" :value=0></el-option>
                        </el-select>
                </el-form-item>
                <el-form-item label="联系方式">
                    <el-input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="11" v-model="form.phoneNum" placeholder="请输入电话，只能是数字"></el-input>
                </el-form-item>
                <el-form-item label="出生日期">
                    <el-col :span="11">
                        <el-date-picker
                                type="date"
                                placeholder="选择日期"
                                v-model="form.birthday"
                                value-format="yyyy-MM-dd"
                                style="width: 250px;"
                        ></el-date-picker>
                    </el-col>
                </el-form-item>
                <el-form-item label="身高">
                    <el-input  v-model="form.height" onkeyup="value=value.replace(/[^\d^\.]/g,'')" oninput="value=value.replace(/[^\d^\.]/g,'')" placeholder="请输入身高CM"></el-input>
                </el-form-item>
                <el-form-item label="体重">
                    <el-input  v-model="form.weight" onkeyup="value=value.replace(/[^\d^\.]/g,'')" oninput="value=value.replace(/[^\d^\.]/g,'')" placeholder="请输入体重KG"></el-input>
                </el-form-item>
                <el-form-item label="地址">
                    <el-input v-model="form.address" placeholder="请输入地址"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 快递框弹出 -->
        <el-dialog title="编辑" :visible.sync="expressVisible" width="30%">
            <el-form ref="form" :model="form" label-width="90px">
                <el-form-item label="ID">
                    <el-input v-model="form.patientId" disabled></el-input>
                </el-form-item>
                <el-form-item label="患者姓名">
                    <el-input v-model="form.patientName" placeholder="请输入姓名" disabled></el-input>
                </el-form-item>
                <el-form-item label="收件人姓名">
                    <el-input v-model="form.addressee" placeholder="请输入收件人姓名"></el-input>
                </el-form-item>
                <el-form-item label="收件人电话">
                    <el-input v-model="form.addresseePhone" placeholder="请输入收件人电话"></el-input>
                </el-form-item>
                <el-form-item label="收件人地址">
                    <el-input v-model="form.toAddress" placeholder="请输入收件人地址"></el-input>
                </el-form-item>
                <el-form-item label="快递单号">
                    <el-input v-model="form.expressNumber" placeholder="请输入快递单号"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.expressRemark" placeholder="请输入备注"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="expressVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveExpressEdit">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 更新快递信息框弹出 -->
        <el-dialog title="编辑" :visible.sync="updateExpressVisible" width="30%">
            <el-form ref="form" :model="form" label-width="90px">
                <el-form-item label="ID">
                    <el-input v-model="form.patientId" disabled></el-input>
                </el-form-item>
                <el-form-item label="患者姓名">
                    <el-input v-model="form.patientName" placeholder="请输入姓名" disabled></el-input>
                </el-form-item>
                <el-form-item label="收件人姓名">
                    <el-input v-model="form.addressee" placeholder="请输入收件人姓名" :disabled="this.form.makeStatus == 5" ></el-input>
                </el-form-item>
                <el-form-item label="收件人电话">
                    <el-input v-model="form.addresseePhone" placeholder="请输入收件人电话" :disabled="this.form.makeStatus == 5"></el-input>
                </el-form-item>
                <el-form-item label="收件人地址">
                    <el-input v-model="form.toAddress" placeholder="请输入收件人地址" :disabled="this.form.makeStatus == 5"></el-input>
                </el-form-item>
                <el-form-item label="快递单号">
                    <el-input v-model="form.expressNumber" placeholder="请输入快递单号" :disabled="this.form.makeStatus == 5"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.expressRemark" placeholder="请输入备注" :disabled="this.form.makeStatus == 5"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer" v-if="this.form.makeStatus == 4">
                <el-button @click="updateExpressVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateExpressEdit">确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="查看结果" :visible.sync="pdfVisible"  :fullscreen="true">
                <iframe :src=this.filePath frameborder="0" style="width: 100vw; height: 80vh"></iframe>
        </el-dialog>
        <el-dialog title="查看报告" :visible.sync="reportPdfVisible"  :fullscreen="true">
            <iframe :src=this.reportPath frameborder="0" style="width: 100vw; height: 80vh"></iframe>
        </el-dialog>
    </div>
</template>

<script>
import request from '../../utils/request';
import { fetchPatientData } from '../../api/patientindex';
import FileSaver from 'file-saver';
import XLSX from 'xlsx';
export default {
    name: 'basetable',
    data() {
        return {
            query: {
                phoneNum: '',
                patientName: '',
                userName: '',
                pageIndex: 1,
                pageSize: 10,
                makeStatu: -1
            },
            tableData: [],
            exportTableData: [],
            multipleSelection: [],
            delList: [],
            editVisible: false,
            addVisible: false,
            passwordVisible: false,
            pdfVisible: false,
            reportPdfVisible: false,
            expressVisible: false,
            updateExpressVisible: false,
            totalRecords: 0,
            exportTotalRecords: 0,
            form: {},
            idx: -1,
            id: -1,
            filePath: "",
            reportPath: ""
        };
    },
    created() {
        this.getData();
    },
    methods: {
        // 获取 easy-mock 的模拟数据
        getData() {
            fetchPatientData(this.query).then(res => {
                this.tableData = res.results;
                this.totalRecords = res.totalRecords || 0;
            });
        },
        exportExcel () {
          /*  this.handleExportSearch();*/
            setTimeout(function () {
                /* generate workbook object from table */
                var xlsxParam = {raw:true};
                var wb = XLSX.utils.table_to_book(document.querySelector('#out-table'),xlsxParam)
                /* get binary string as output */
                var wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' })
                try {
                    FileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), '患者信息表.xlsx')
                } catch (e) { if (typeof console !== 'undefined') console.log(e, wbout) }
                return wbout
            },3000);
        },
        // 触发搜索按钮
        handleExportSearch() {
            this.$set(this.query, 'pageIndex', 1);
            this.getExportData();
        },
        // 获取 easy-mock 的模拟数据
        getExportData() {
            this.query.pageSize = 1000000;
            fetchPatientData(this.query).then(res => {
                this.exportTableData = res.results;
                this.exportTotalRecords = res.totalRecords || 0;
                this.exportExcel();
                this.query.pageSize = 10;
            });
        },
        changeTableSort(column){
            if (column.order === 'descending') {
                this.query.sortby = column.prop
                this.query.orderBy = 'DESC'
            } else {
                this.query.sortby = column.prop
                this.query.orderBy = 'ASC'
            }
            this.getData();
        },
        // 触发搜索按钮
        handleSearch() {
            this.$set(this.query, 'pageIndex', 1);
            this.getData();
        },
        // 删除操作
        handleDelete(index, row) {
            // 二次确认删除
            this.$confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    this.form = row;
                    var patientParam = {patientId:this.form.patientId};
                    const deleteResult = query => {
                        return request({
                            url: '/api/patient/delete',
                            method: 'post',
                            params: patientParam
                        });
                    };

                    deleteResult(this.form).then(res => {
                        var code = res.code;
                        var errorMessage = res.errorMessage;
                        if(code == 200){
                            this.$message.success('删除成功');
                        }else{
                            this.$message.error(errorMessage);
                        }
                    });

                    this.tableData.splice(index, 1);
                })
                .cancel(() => {})
                .catch(() => { this.$message.error("哎呀，出错啦！");});
        },
        // 多选操作
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        delAllSelection() {
            const length = this.multipleSelection.length;
            let str = '';
            this.delList = this.delList.concat(this.multipleSelection);
            for (let i = 0; i < length; i++) {
                if(i != (length - 1)){
                    str += this.multipleSelection[i].patientId + ',';
                }else{
                    str += this.multipleSelection[i].patientId ;
                }
            }
           var patient = {patientId:str};
            const batchdeleteResult = query => {
                return request({
                    url: '/api/patient/batchdelete',
                    method: 'delete',
                    headers: {'Content-Type': 'application/json;charset=UTF-8'},
                    params: patient
                });
            };

            batchdeleteResult(this.form).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.$message.success('删除成功');
                }else{
                    this.$message.error(errorMessage);
                }
            });
            this.multipleSelection = [];
            this.getData();
        },

        // 查看详情
        handledetail(path) {
            this.filePath =window.location.protocol + "//" + window.location.host + "/" + path;
            this.pdfVisible = true;
        },
        // 查看报告
        handleReport(path) {
            this.reportPath =window.location.protocol + "//" + window.location.host + "/" + path;
            this.reportPdfVisible = true;
        },
        //查看制作账号是否真的有查看结果的权限
        hasTurePerm(){
            var hasPrive = false;
            const queryResult = query => {
                return request({
                    url: '/api/patient/createAccount/priv',
                    method: 'post',
                });
            };

            queryResult().then(res => {
                var code = res.code;
                if(code == 200){
                    hasPrive = res.data;
                }
            });
            return hasPrive;
        },
        // 修改鞋垫状态
        handleStatus(index, row) {
            var makeStatus = row.makeStatus;
            if(makeStatus == 3){
                this.idx = index;
                this.form = row;
                this.expressVisible = true;
            }else{
                var jsonObj = {'patientId': row.patientId,'makeStatus':row.makeStatus}
                const updateResult = query => {
                    return request({
                        url: '/api/patient/update/insoleState',
                        method: 'post',
                        params: jsonObj,
                    });
                };

                updateResult(this.form).then(res => {
                    var code = res.code;
                    var errorMessage = res.errorMessage;
                    if(code == 200){
                        this.$message.success(`修改成功`);
                        this.$set(this.tableData, this.idx, this.form);
                        this.getData();
                    }else{
                        this.$message.error(errorMessage);
                    }
                });
            }
        },
        //保存快递信息
        saveExpressEdit(){
            this.expressVisible = false;
            const updateResult = query => {
                query.foots = null;
                return request({
                    url: '/api/patient/update/express',
                    method: 'post',
                    params: query,
                });
            };

            updateResult(this.form).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.$message.success(`修改成功`);
                    this.$set(this.tableData, this.idx, this.form);
                    this.getData();
                }else{
                    this.$message.error(errorMessage);
                }
            });
        },
        //更新快递信息
        updateExpressEdit(){
            this.updateExpressVisible = false;
            const updateResult = query => {
                query.foots = null;
                return request({
                    url: '/api/patient/update/express',
                    method: 'post',
                    params: query,
                });
            };
            this.form.makeStatus = this.form.makeStatus - 1;
            updateResult(this.form).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.$message.success(`修改成功`);
                    this.$set(this.tableData, this.idx, this.form);
                    this.getData();
                }else{
                    this.$message.error(errorMessage);
                }
            });
        },
        showExpress(index, row){
            this.idx = index;
            this.form = row;
            this.updateExpressVisible = true;
            this.getData();
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
                    url: '/api/patient/update',
                    method: 'post',
                    params: query,
                });
            };

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
    width: 200px;
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
