<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 后台用户
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
              <!--  <el-button
                    type="primary"
                    icon="el-icon-delete"
                    class="handle-del mr10"
                    @click="delAllSelection"
                    v-if="hasPerm('sysuser:batchdelete')"
                >批量删除</el-button>-->
                <el-input v-model="query.userName" placeholder="账号" class="handle-input mr10"></el-input>
                <el-input v-model="query.phoneNum" placeholder="联系方式" class="handle-input mr10"></el-input>
                <el-select v-model="query.accountType" placeholder="账号类型" class="handle-select mr10">
                    <el-option  label="请选择账号类型" :value=-1 ></el-option>
                    <el-option  label="超级账号" :value=0 ></el-option>
                    <!-- <el-option  label="运营账号" :value=1 v-if="accountType != 1"></el-option>
                     <el-option  label="财务账号" :value=2 v-if="accountType != 1"></el-option>
                     <el-option  label="制作账号" :value=3 v-if="accountType != 1 && accountType != 5"></el-option>
                     <el-option  label="快递账号" :value=4 v-if="accountType != 1 && accountType != 5"></el-option>-->
                    <el-option  label="B级账号" :value=5 ></el-option>
                    <el-option  label="A级账号" :value=6 ></el-option>
                    <!--<el-option  label="制作服务商" :value=7 v-if="accountType != 1 && accountType != 5"></el-option>-->
                </el-select>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                <el-button type="primary" icon="el-icon-plus" @click="handleAdd" v-if="hasPerm('sysuser:add')">新增</el-button>
            </div>
            <el-table
                :data="tableData"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
                @selection-change="handleSelectionChange"
                @row-dblclick="handleDbClickUser"
            >
              <!--  <el-table-column type="selection" width="55" align="center"></el-table-column>-->
                <el-table-column prop="userId" label="ID" width="55" align="center"></el-table-column>
                <el-table-column prop="userName" width="80" label="账号"></el-table-column>
                <el-table-column prop="phoneNum" width="120" label="联系方式"></el-table-column>
               <!-- <el-table-column prop="nickname" label="昵称"></el-table-column>-->
                <el-table-column prop="email" label="邮箱"></el-table-column>
                <el-table-column width="100" label="账号类型">
                    <template slot-scope="scope">
                        <el-tag effect="dark" v-if="scope.row.accountType=='0'" type="danger">超级账号</el-tag>
                        <el-tag effect="dark" v-else-if="scope.row.accountType=='1'" type="warning">运营账号</el-tag>
                        <el-tag effect="dark" v-else-if="scope.row.accountType=='2'" type="info">财务账号</el-tag>
                        <el-tag effect="dark" v-else-if="scope.row.accountType=='3'" type="success">制作账号</el-tag>
                        <el-tag effect="dark" v-else-if="scope.row.accountType=='4'" type="success">快递账号</el-tag>
                        <el-tag effect="dark" v-else-if="scope.row.accountType=='5'" type="success">B级账号</el-tag>
                        <el-tag effect="dark" v-else-if="scope.row.accountType=='6'" type="success">A级账号</el-tag>
                        <el-tag effect="dark" v-else-if="scope.row.accountType=='7'" type="success">制作服务商</el-tag>
                        <el-tag effect="dark" v-else type="warning">不存在类型</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="remark" label="备注"></el-table-column>
                <el-table-column width="100" label="状态">
                    <template slot-scope="scope">
                        <el-tag effect="dark" v-if="scope.row.status=='N'" type="success">启用</el-tag>
                        <el-tag effect="dark" v-if="scope.row.status=='D'" type="danger">禁用</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="280" align="center">
                    <template slot-scope="scope">
                        <el-button
                            type="text"
                            icon="el-icon-edit"
                            @click="handleEdit(scope.$index, scope.row)"
                            v-if="hasPerm('sysuser:edit')"
                        >编辑</el-button>
                        <el-button
                            type="text"
                            icon="el-icon-delete"
                            class="red"
                            @click="handleStatu(scope.$index, scope.row)"
                            v-if="hasPerm('sysuser:disableStatus') && scope.row.status == 'N'"
                        >禁用</el-button>
                        <el-button
                                type="text"
                                icon="el-icon-delete"
                                class="red"
                                @click="handleStatu(scope.$index, scope.row)"
                                v-if="hasPerm('sysuser:enableStatus') && scope.row.status == 'D'"
                        >启用</el-button>
                        <el-button
                                type="text"
                                icon="el-icon-delete"
                                @click="handlePasswordEdit(scope.$index, scope.row)"
                                v-if="hasPerm('sysuser:resetpass')"
                        >重置密码</el-button>
                        <el-button
                                type="text"
                                icon="el-icon-info"
                                @click="handleUser(scope.$index, scope.row)"
                                v-if="hasPerm('sysuser:usersUnderName') && scope.row.accountType == 5"
                        >名下机器</el-button>
                        <el-button
                                type="text"
                                icon="el-icon-info"
                                @click="handleSysUser(scope.$index, scope.row)"
                                v-if="hasPerm('sysuser:sysUsersUnderName') && scope.row.accountType == 6"
                        >名下账号</el-button>
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
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="ID">
                    <el-input v-model="form.userId" disabled></el-input>
                </el-form-item>
                <el-form-item label="账号">
                    <el-input v-model="form.userName" placeholder="请输入账号" disabled></el-input>
                </el-form-item>
                <el-form-item label="联系方式">
                    <el-input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="11" v-model="form.phoneNum" placeholder="请输入电话，只能是数字"></el-input>
                </el-form-item>
              <!--  <el-form-item label="昵称">
                    <el-input v-model="form.nickname" placeholder="请输入昵称"></el-input>
                </el-form-item>-->
                <el-form-item label="邮箱">
                    <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark" placeholder="请输入备注"></el-input>
                </el-form-item>
                <el-form-item label="账号类型" required v-if="form.accountType == 5" >
                    <el-select v-model="form.accountType" placeholder="请选择"  disabled>
                        <el-option  label="超级账号" :value=0 v-if="accountType != 1 && accountType != 6 && accountType != 5"></el-option>
                       <!-- <el-option  label="运营账号" :value=1 v-if="accountType != 1"></el-option>
                        <el-option  label="财务账号" :value=2 v-if="accountType != 1"></el-option>
                        <el-option  label="制作账号" :value=3 v-if="accountType != 1 && accountType != 5"></el-option>
                        <el-option  label="快递账号" :value=4 v-if="accountType != 1 && accountType != 5"></el-option>-->
                        <el-option  label="B级账号" :value=5 v-if="accountType != 5"></el-option>
                        <el-option  label="A级账号" :value=6 v-if="accountType != 1 && accountType != 6 && accountType != 5"></el-option>
                        <!--<el-option  label="制作服务商" :value=7 v-if="accountType != 1 && accountType != 5"></el-option>-->
                    </el-select>
                </el-form-item>

                <el-form-item label="账号类型" required v-if="form.accountType != 5">
                    <el-select v-model="form.accountType" placeholder="请选择">
                        <el-option  label="超级账号" :value=0 v-if="accountType != 1 && accountType != 6 && accountType != 5"></el-option>
                        <!-- <el-option  label="运营账号" :value=1 v-if="accountType != 1"></el-option>
                         <el-option  label="财务账号" :value=2 v-if="accountType != 1"></el-option>
                         <el-option  label="制作账号" :value=3 v-if="accountType != 1 && accountType != 5"></el-option>
                         <el-option  label="快递账号" :value=4 v-if="accountType != 1 && accountType != 5"></el-option>-->
                        <el-option  label="B级账号" :value=5 v-if="accountType != 5"></el-option>
                        <el-option  label="A级账号" :value=6 v-if="accountType != 1 && accountType != 6 && accountType != 5"></el-option>
                        <!--<el-option  label="制作服务商" :value=7 v-if="accountType != 1 && accountType != 5"></el-option>-->
                    </el-select>
                </el-form-item>
                <el-form-item label="所属账号" required v-if="hasPerm('sysuser:userIdCreate')">
                    <el-select v-model="form.userIdCreate" placeholder="请选择" >
                        <el-option v-for="item in this.rootName" :key="item.userId" :label="item.userName" :value="item.userId">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false,form = {}">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 重置密码框 -->
        <el-dialog title="重置密码" :visible.sync="passwordVisible" width="30%">
            <el-form ref="form" :model="form" label-width="70px">
                <el-form-item label="用户名">
                    <el-input v-model="form.userName" placeholder="请输入用户名" disabled></el-input>
                </el-form-item>
                <el-form-item label="联系方式">
                    <el-input type="text" v-model="form.phoneNum" disabled></el-input>
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="form.password" maxlength="40" placeholder="请输入密码" value="******">******</el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="passwordVisible = false">取 消</el-button>
                <el-button type="primary" @click="savePass">确 定</el-button>
            </span>
        </el-dialog>


        <!-- 新增弹出框 -->
        <el-dialog title="新增" :visible.sync="addVisible" width="30%">
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="账号" required>
                    <el-input v-model="form.userName" placeholder="请输入账号"></el-input>
                </el-form-item>
                <el-form-item label="联系方式">
                    <el-input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="11" v-model="form.phoneNum" placeholder="请输入电话，只能是数字"></el-input>
                </el-form-item>
                <el-form-item label="密码" required>
                    <el-input v-model="form.password" placeholder="请输入密码"></el-input>
                </el-form-item>
               <!-- <el-form-item label="昵称">
                    <el-input v-model="form.nickname" placeholder="请输入昵称"></el-input>
                </el-form-item>-->
                <el-form-item label="邮箱">
                    <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark" placeholder="请输入备注"></el-input>
                </el-form-item>
                <el-form-item label="账号类型" required >
                    <el-select v-model="form.accountType" placeholder="请选择">
                        <el-option  label="超级账号" :value=0 v-if="accountType != 1 && accountType != 6 && accountType != 5"></el-option>
                       <!-- <el-option  label="运营账号" :value=1 v-if="accountType != 1"></el-option>
                        <el-option  label="财务账号" :value=2 v-if="accountType != 1"></el-option>
                        <el-option  label="制作账号" :value=3 v-if="accountType != 1 && accountType != 5"></el-option>
                        <el-option  label="快递账号" :value=4 v-if="accountType != 1 && accountType != 5"></el-option>-->
                        <el-option  label="B级账号" :value=5 v-if="accountType != 5"></el-option>
                        <el-option  label="A级账号" :value=6 v-if="accountType != 1 && accountType != 6 && accountType != 5"></el-option>
                        <!--<el-option  label="制作服务商" :value=7 v-if="accountType != 1 && accountType != 5"></el-option>-->
                    </el-select>
                </el-form-item>
                <el-form-item label="所属账号" required v-if="hasPerm('sysuser:userIdCreate')">
                    <el-select v-model="form.userIdCreate" placeholder="请选择" >
                        <el-option v-for="item in this.rootName" :key="item.userId" :label="item.userName" :value="item.userId">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveAdd">确 定</el-button>
            </span>
        </el-dialog>


        <el-dialog title="用户信息" :visible.sync="userVisible" :fullscreen="true"  @close="handleUserColse">
            <div class="container">
                <div class="handle-box">
                    <el-button
                            type="primary"
                            icon="el-icon-delete"
                            class="handle-del mr10"
                            @click="delAllUserSelection"
                            v-if="hasPerm('table:batchdelete')"
                    >批量删除</el-button>
                    <el-input v-model="query.userName" placeholder="用户名" class="handle-input mr10"></el-input>
                    <el-input v-model="query.phoneNum" placeholder="联系方式" class="handle-input mr10"></el-input>
                    <el-input v-model="query.sysUserName" placeholder="运营商" class="handle-input mr10" disabled></el-input>
                    <el-button type="primary" icon="el-icon-search" @click="handleUserSearch">搜索</el-button>
                    <el-button type="primary" icon="el-icon-plus" @click="handleUserAdd" v-if="hasPerm('table:add')">新增</el-button>
                </div>
                <el-table
                        :data="userData"
                        border
                        class="table"
                        ref="multipleTable"
                        header-cell-class-name="table-header"
                        @selection-change="handleSelectionChange"
                >
                    <el-table-column type="selection" width="55" align="center"></el-table-column>
                    <el-table-column prop="userId" label="ID" width="55" align="center"></el-table-column>
                    <el-table-column prop="userName" label="用户名" width="100"></el-table-column>
                    <el-table-column label="头像" align="center">
                        <template slot-scope="scope">
                            <el-image
                                    class="table-td-thumb"
                                    :src= "getImgUrl(scope.row.headPortrait)"
                            ></el-image>
                        </template>
                    </el-table-column>
                    <el-table-column prop="phoneNum" label="联系方式">
                        <!--  <template slot-scope="scope">{{scope.row.phoneNum}}</template>-->
                    </el-table-column>
                    <el-table-column prop="companyName" label="公司名称"></el-table-column>
                    <el-table-column prop="address" label="地址"></el-table-column>
                    <el-table-column width="100" label="权限">
                        <template slot-scope="scope">
                            <el-tag effect="dark" v-if="scope.row.privilege=='0'" type="danger">禁用</el-tag>
                            <el-tag effect="dark" v-if="scope.row.privilege=='1'" type="success">采集&报告</el-tag>
                            <el-tag effect="dark" v-if="scope.row.privilege=='2'" type="warning">显示结果</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="300" align="center">
                        <template slot-scope="scope">
                            <el-button
                                    type="text"
                                    icon="el-icon-edit"
                                    @click="handleUserEdit(scope.$index, scope.row)"
                                    v-if="hasPerm('table:edit')"
                            >编辑</el-button>
                            <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    class="red"
                                    @click="handleUserDelete(scope.$index, scope.row)"
                                    v-if="hasPerm('table:delete')"
                            >删除</el-button>
                            <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    @click="handleUserPasswordEdit(scope.$index, scope.row)"
                                    v-if="hasPerm('table:resetpass')"
                            >重置密码</el-button>
                            <el-button
                                    type="text"
                                    icon="el-icon-info"
                                    @click="handlePrice(scope.$index, scope.row)"
                                    v-if="hasPerm('table:price')"
                            >设置价格</el-button>
                            <el-button
                                    type="text"
                                    icon="el-icon-info"
                                    @click="handlePatient(scope.$index, scope.row)"
                                    v-if="hasPerm('table:patient')"
                            >查看患者</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pagination">
                    <el-pagination
                            background
                            layout="total, prev, pager, next"
                            :current-page="query.pageIndex"
                            :page-size="query.pageSize"
                            :total="userTotalRecords"
                            @current-change="handlePageChange"
                    ></el-pagination>
                </div>
            </div>
        </el-dialog>

        <!-- 新增弹出框 -->
        <el-dialog title="新增" :visible.sync="addUserVisible" width="30%">
            <el-form ref="form" :model="form" label-width="70px">
                <el-form-item label="用户名" required>
                    <el-input v-model="form.userName" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item label="密码" required>
                    <el-input v-model="form.password" placeholder="请输入密码"></el-input>
                </el-form-item>
                <el-form-item label="联系方式" required>
                    <el-input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="11" v-model="form.phoneNum" placeholder="请输入电话，只能是数字"></el-input>
                </el-form-item>
                <el-form-item label="公司名称" required>
                    <el-input v-model="form.companyName" placeholder="请输入公司名称"></el-input>
                </el-form-item>
                <el-form-item label="地址" required>
                    <el-input v-model="form.address" placeholder="请输入地址"></el-input>
                </el-form-item>
                <el-form-item label="权限" required>
                    <el-select v-model="form.privilege" placeholder="请选择" >
                        <el-option key="0" label="禁用" value="0"></el-option>
                        <el-option key="1" label="采集&报告" value="1"></el-option>
                        <el-option key="2" label="显示结果" value="2"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="B级账号" required>
                    <el-select v-model="form.sysUserName" placeholder="请选择" >
                        <el-option v-for="item in this.serviceName" :key="item" :label="item" :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addUserVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveUserAdd">确 定</el-button>
            </span>
        </el-dialog>

        <!-- User编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editUserVisible" width="30%">
            <el-form ref="form" :model="form" label-width="70px">
                <el-form-item label="ID">
                    <el-input v-model="form.userId" disabled></el-input>
                </el-form-item>
                <el-form-item label="用户名">
                    <el-input v-model="form.userName" placeholder="请输入用户名" disabled></el-input>
                </el-form-item>
                <!-- <el-form-item label="密码">
                     <el-input v-model="form.password" maxlength="40" placeholder="请输入密码" value="******" disabled>******</el-input>
                 </el-form-item>-->
                <el-form-item label="联系方式" required>
                    <el-input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="11" v-model="form.phoneNum" placeholder="请输入电话，只能是数字"></el-input>
                </el-form-item>
                <el-form-item label="公司名称" required>
                    <el-input v-model="form.companyName" placeholder="请输入公司名称"></el-input>
                </el-form-item>
                <el-form-item label="地址" required>
                    <el-input v-model="form.address" placeholder="请输入地址"></el-input>
                </el-form-item>
                <el-form-item label="权限">
                    <el-select v-model="form.privilege" placeholder="请选择">
                        <el-option key='0' label="禁用" value=0></el-option>
                        <el-option key='1' label="采集&报告" value=1></el-option>
                        <el-option key='2' label="显示结果" value=2></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="B级账号" required>
                    <el-select v-model="form.sysUserName" placeholder="请选择" >
                        <el-option v-for="item in this.serviceName" :key="item" :label="item" :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editUserVisible = false,form = {}">取 消</el-button>
                <el-button type="primary" @click="saveUserEdit">确 定</el-button>
            </span>
        </el-dialog>
        <!-- User重置密码框 -->
        <el-dialog title="重置密码" :visible.sync="userPasswordVisible" width="30%">
            <el-form ref="form" :model="form" label-width="70px">
                <el-form-item label="用户名">
                    <el-input v-model="form.userName" placeholder="请输入用户名" disabled></el-input>
                </el-form-item>
                <el-form-item label="联系方式">
                    <el-input type="text" v-model="form.phoneNum" disabled></el-input>
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="form.password" maxlength="40" placeholder="请输入密码" value="******">******</el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="userPasswordVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveUserPass">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 价格框弹出 -->
        <el-dialog title="编辑" :visible.sync="priceVisible" width="45%">
            <el-row>
                <el-col :span="8">
                    项目
                </el-col>
                <el-col :span="8">
                    基础价格
                </el-col>
                <el-col :span="8">
                    进阶价格
                </el-col>
            </el-row>
            <pre></pre>
            <br/>
            <!-- 0篮球 1足球 2跑步 3滑雪 4高尔夫 5骑行 6室内运动 7舒适 8皮鞋 9异形加高-->
            <el-form ref="form" :model="priceForm" label-width="90px" >
                <el-row v-for="item in priceForm.results">
                    <el-col :span="4" v-if="item.project == 0">
                        篮球
                    </el-col>
                    <el-col :span="4" v-if="item.project == 1">
                        足球
                    </el-col>
                    <el-col :span="4" v-if="item.project == 2">
                        跑步
                    </el-col>
                    <el-col :span="4" v-if="item.project == 3">
                        滑雪
                    </el-col>
                    <el-col :span="4" v-if="item.project == 4">
                        高尔夫
                    </el-col>
                    <el-col :span="4" v-if="item.project == 5">
                        骑行
                    </el-col>
                    <el-col :span="4" v-if="item.project == 6">
                        室内运动
                    </el-col>
                    <el-col :span="4" v-if="item.project == 7">
                        舒适
                    </el-col>
                    <el-col :span="4" v-if="item.project == 8">
                        皮鞋
                    </el-col>
                    <el-col :span="4" v-if="item.project == 9">
                        异形加高
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="价格">
                            <el-input type="number" @keydown="handleInput2" v-model="item.price" placeholder="请输入价格"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="价格">
                            <el-input type="number" @keydown="handleInput2" v-model="item.advancedPrice" placeholder="请输入价格"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="priceVisible = false">取 消</el-button>
                <el-button type="primary" @click="savePriceEdit">确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="账号信息" :visible.sync="sysUserVisible" :fullscreen="true"  @close="handleSysUserColse">
            <div class="container">
                <div class="handle-box">
                    <!--  <el-button
                          type="primary"
                          icon="el-icon-delete"
                          class="handle-del mr10"
                          @click="delAllSelection"
                          v-if="hasPerm('sysuser:batchdelete')"
                      >批量删除</el-button>-->
                    <el-input v-model="query.userName" placeholder="账号" class="handle-input mr10"></el-input>
                    <el-input v-model="query.phoneNum" placeholder="联系方式" class="handle-input mr10"></el-input>
                    <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                    <el-button type="primary" icon="el-icon-plus" @click="handleAdd" v-if="hasPerm('sysuser:add')">新增</el-button>
                </div>
                <el-table
                        :data="tableData"
                        border
                        class="table"
                        ref="multipleTable"
                        header-cell-class-name="table-header"
                        @selection-change="handleSelectionChange"
                        @row-dblclick="handleDbClickUser"
                >
                    <!--  <el-table-column type="selection" width="55" align="center"></el-table-column>-->
                    <el-table-column prop="userId" label="ID" width="55" align="center"></el-table-column>
                    <el-table-column prop="userName" width="80" label="账号"></el-table-column>
                    <el-table-column prop="phoneNum" width="120" label="联系方式"></el-table-column>
                    <!-- <el-table-column prop="nickname" label="昵称"></el-table-column>-->
                    <el-table-column prop="email" label="邮箱"></el-table-column>
                    <el-table-column width="100" label="账号类型">
                        <template slot-scope="scope">
                            <el-tag effect="dark" v-if="scope.row.accountType=='0'" type="danger">超级账号</el-tag>
                            <el-tag effect="dark" v-else-if="scope.row.accountType=='1'" type="warning">运营账号</el-tag>
                            <el-tag effect="dark" v-else-if="scope.row.accountType=='2'" type="info">财务账号</el-tag>
                            <el-tag effect="dark" v-else-if="scope.row.accountType=='3'" type="success">制作账号</el-tag>
                            <el-tag effect="dark" v-else-if="scope.row.accountType=='4'" type="success">快递账号</el-tag>
                            <el-tag effect="dark" v-else-if="scope.row.accountType=='5'" type="success">B级账号</el-tag>
                            <el-tag effect="dark" v-else-if="scope.row.accountType=='6'" type="success">A级账号</el-tag>
                            <el-tag effect="dark" v-else-if="scope.row.accountType=='7'" type="success">制作服务商</el-tag>
                            <el-tag effect="dark" v-else type="warning">不存在类型</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="remark" label="备注"></el-table-column>
                    <el-table-column width="100" label="状态">
                        <template slot-scope="scope">
                            <el-tag effect="dark" v-if="scope.row.status=='N'" type="success">启用</el-tag>
                            <el-tag effect="dark" v-if="scope.row.status=='D'" type="danger">禁用</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="280" align="center">
                        <template slot-scope="scope">
                            <el-button
                                    type="text"
                                    icon="el-icon-edit"
                                    @click="handleEdit(scope.$index, scope.row)"
                                    v-if="hasPerm('sysuser:edit')"
                            >编辑</el-button>
                            <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    class="red"
                                    @click="handleStatu(scope.$index, scope.row)"
                                    v-if="hasPerm('sysuser:disableStatus') && scope.row.status == 'N'"
                            >禁用</el-button>
                            <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    class="red"
                                    @click="handleStatu(scope.$index, scope.row)"
                                    v-if="hasPerm('sysuser:enableStatus') && scope.row.status == 'D'"
                            >启用</el-button>
                            <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    @click="handlePasswordEdit(scope.$index, scope.row)"
                                    v-if="hasPerm('sysuser:resetpass')"
                            >重置密码</el-button>
                            <el-button
                                    type="text"
                                    icon="el-icon-info"
                                    @click="handleUser(scope.$index, scope.row)"
                                    v-if="hasPerm('sysuser:usersUnderName') && scope.row.accountType == 5"
                            >名下机器</el-button>
                            <el-button
                                    type="text"
                                    icon="el-icon-info"
                                    @click="handleSysUser(scope.$index, scope.row)"
                                    v-if="hasPerm('sysuser:sysUsersUnderName') && scope.row.accountType == 6"
                            >名下账号</el-button>
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
        </el-dialog>

        <el-dialog title="患者信息" :visible.sync="patientVisible" :fullscreen="true"  @close="handlePatientColse">
            <div class="container">
                <div class="handle-box">
                    <el-button
                            type="primary"
                            icon="el-icon-delete"
                            class="handle-del mr10"
                            @click="delAllPatientSelection"
                            v-if="hasPerm('patientinfo:batchdelete')"
                    >批量删除</el-button>
                    <el-input v-model="query.patientName" placeholder="患者姓名" class="handle-input mr10"></el-input>
                    <el-input v-model="query.phoneNum" placeholder="联系方式" class="handle-input mr10"></el-input>
                    <el-input v-model="query.userName" placeholder="创建用户" disabled class="handle-input mr10"></el-input>
                    <el-select v-model="query.makeStatu" placeholder="制作状态" class="handle-select mr10">
                        <el-option  label="请选择状态" :value=-1 ></el-option>
                        <el-option  label="不制作" :value=0 ></el-option>
                        <el-option  label="待制作" :value=1 ></el-option>
                        <el-option  label="制作中" :value=2 ></el-option>
                        <el-option  label="制作完成" :value=3 ></el-option>
                        <el-option  label="邮递中" :value=4 ></el-option>
                        <el-option  label="已签收" :value=5 ></el-option>
                    </el-select>
                    <el-button type="primary" icon="el-icon-search" @click="handlePatientSearch">搜索</el-button>
                    <el-button type="primary" icon="el-icon-search" @click="handleExportSearch">导出</el-button>
                </div>
                <el-table
                        :data="patientData"
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
                    <el-table-column prop="phoneNum" width="150" label="联系方式">
                    </el-table-column>
                    <el-table-column prop="birthday" label="出生日期"></el-table-column>
                    <el-table-column prop="height"  label="身高(CM)"></el-table-column>
                    <el-table-column prop="weight"  label="体重(KG)"></el-table-column>
                    <el-table-column prop="address" label="地址"></el-table-column>
                    <!-- <el-table-column prop="filePath" label="地址"></el-table-column>-->
                    <el-table-column prop="userName" width="100" label="创建用户"></el-table-column>
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
                    <el-table-column prop="houtaiRemark" width="100" label="备注"></el-table-column>
                    <!--  <el-table-column width="80" label="用途">
                          <template slot-scope="scope">
                              <el-tag effect="dark" v-if="scope.row.makePurpose=='0'" type="danger">篮球</el-tag>
                              <el-tag effect="dark" v-else-if="scope.row.makePurpose=='1'" type="warning">足球</el-tag>
                              <el-tag effect="dark" v-else-if="scope.row.makePurpose=='2'" type="info">跑步</el-tag>
                              <el-tag effect="dark" v-else-if="scope.row.makePurpose=='3'" type="success">滑雪</el-tag>
                              <el-tag effect="dark" v-else-if="scope.row.makePurpose=='4'" type="success">高尔夫</el-tag>
                              <el-tag effect="dark" v-else-if="scope.row.makePurpose=='5'" type="success">骑行</el-tag>
                              <el-tag effect="dark" v-else-if="scope.row.makePurpose=='6'" type="success">室内运动</el-tag>
                              <el-tag effect="dark" v-else-if="scope.row.makePurpose=='7'" type="success">舒适</el-tag>
                              <el-tag effect="dark" v-else-if="scope.row.makePurpose=='8'" type="success">皮鞋</el-tag>
                              <el-tag effect="dark" v-else type="warning">无</el-tag>
                          </template>
                      </el-table-column>-->
                    <el-table-column label="操作" width="300" align="center">
                        <template slot-scope="scope">
                            <el-button
                                    type="text"
                                    icon="el-icon-edit"
                                    @click="handlePatientEdit(scope.$index, scope.row)"
                                    v-if="hasPerm('patientinfo:edit')"
                            >编辑</el-button>
                            <el-button
                                    type="text"
                                    icon="el-icon-edit"
                                    @click="handlePatientEditRemark(scope.$index, scope.row)"
                            >编辑备注</el-button>
                            <el-button
                                    type="text"
                                    icon="el-icon-delete"
                                    class="red"
                                    @click="handlePatientDelete(scope.$index, scope.row)"
                                    v-if="hasPerm('patientinfo:delete')"
                            >删除</el-button>
                            <!-- <el-button
                                     type="text"
                                     icon="el-icon-edit"
                                     v-if="scope.row.makeStatus=='0' && hasPerm('patientinfo:toCreate')"
                                     @click="handleStatus(scope.$index, scope.row)"
                             >去制作
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
                                    v-if="scope.row.makeStatus=='4'"
                                    @click="onlyShowExpress(scope.$index, scope.row)">
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
                            :total="patientTotalRecords"
                            @current-change="handlePatientPageChange"
                    ></el-pagination>
                </div>
            </div>
        </el-dialog>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editPatientVisible" width="30%">
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
                    <el-input v-model="form.height" onkeyup="value=value.replace(/[^\d^\.]/g,'')" placeholder="请输入身高CM"></el-input>
                </el-form-item>
                <el-form-item label="体重">
                    <el-input v-model="form.weight" onkeyup="value=value.replace(/[^\d^\.]/g,'')" placeholder="请输入体重KG"></el-input>
                </el-form-item>
                <el-form-item label="地址">
                    <el-input v-model="form.address" placeholder="请输入地址"></el-input>
                </el-form-item>
    <!--            <el-form-item label="备注">
                    <el-input v-model="form.houtaiRemark" placeholder="请输入备注"></el-input>
                </el-form-item>-->
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editPatientVisible = false,form = {}">取 消</el-button>
                <el-button type="primary" @click="savePatientEdit">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 编辑备注弹出框 -->
        <el-dialog title="编辑" :visible.sync="editPatientRemarkVisible" width="30%">
            <el-form ref="form" :model="form" label-width="70px">
                <el-form-item label="ID">
                    <el-input v-model="form.patientId" disabled></el-input>
                </el-form-item>
                <el-form-item label="姓名">
                    <el-input v-model="form.patientName" placeholder="请输入姓名" disabled></el-input>
                </el-form-item>
                <el-form-item label="性别">
                    <el-select v-model="form.sex" placeholder="请选择" disabled>
                        <el-option  label="男" :value=1></el-option>
                        <el-option  label="女" :value=0></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="联系方式">
                    <el-input type="text" disabled onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="11" v-model="form.phoneNum" placeholder="请输入电话，只能是数字"></el-input>
                </el-form-item>
                <el-form-item label="出生日期">
                    <el-col :span="11">
                        <el-date-picker
                                type="date"
                                placeholder="选择日期"
                                v-model="form.birthday"
                                value-format="yyyy-MM-dd"
                                style="width: 250px;"
                                disabled
                        ></el-date-picker>
                    </el-col>
                </el-form-item>
                <el-form-item label="身高">
                    <el-input v-model="form.height" disabled onkeyup="value=value.replace(/[^\d^\.]/g,'')" placeholder="请输入身高CM"></el-input>
                </el-form-item>
                <el-form-item label="体重">
                    <el-input v-model="form.weight" disabled onkeyup="value=value.replace(/[^\d^\.]/g,'')" placeholder="请输入体重KG"></el-input>
                </el-form-item>
                <el-form-item label="地址">
                    <el-input v-model="form.address" disabled placeholder="请输入地址"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.houtaiRemark" placeholder="请输入备注"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editPatientRemarkVisible = false,form = {}">取 消</el-button>
                <el-button type="primary" @click="savePatientRemarkEdit">确 定</el-button>
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
            </el-form>
            <span slot="footer" class="dialog-footer" v-if="this.form.makeStatus == 4">
                <el-button @click="updateExpressVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateExpressEdit">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 仅查看快递信息框弹出 -->
        <el-dialog title="编辑" :visible.sync="showExpressVisible" width="30%">
            <el-form ref="form" :model="form" label-width="90px">
                <el-form-item label="ID">
                    <el-input v-model="form.patientId" disabled></el-input>
                </el-form-item>
                <el-form-item label="患者姓名">
                    <el-input v-model="form.patientName" placeholder="请输入姓名" disabled></el-input>
                </el-form-item>
                <el-form-item label="收件人姓名">
                    <el-input v-model="form.addressee" placeholder="请输入收件人姓名" disabled ></el-input>
                </el-form-item>
                <el-form-item label="收件人电话">
                    <el-input v-model="form.addresseePhone" placeholder="请输入收件人电话" disabled></el-input>
                </el-form-item>
                <el-form-item label="收件人地址">
                    <el-input v-model="form.toAddress" placeholder="请输入收件人地址" disabled></el-input>
                </el-form-item>
                <el-form-item label="快递单号">
                    <el-input v-model="form.expressNumber" placeholder="请输入快递单号" disabled></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.expressRemark" placeholder="请输入备注" disabled></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer" v-if="this.form.makeStatus == 4">
                <el-button @click="showExpressVisible = false">取 消</el-button>
            </span>
        </el-dialog>

        <el-dialog title="查看报告" :visible.sync="reportPdfVisible"  :fullscreen="true">
            <iframe :src=this.reportPath frameborder="0" style="width: 100vw; height: 80vh"></iframe>
        </el-dialog>
        <el-dialog title="查看结果" :visible.sync="pdfVisible"  :fullscreen="true">
            <iframe :src=this.filePath frameborder="0" style="width: 100vw; height: 80vh"></iframe>
        </el-dialog>
    </div>
</template>

<script>
import request from '../../utils/request';
import { fetchSysuserData } from '../../api/sysuserindex';
import { fetchData } from '../../api/index';
import { fetchPatientData } from '../../api/patientindex';
import { hasPerm } from '../../main';
export default {
    name: 'basetable',
    data() {
        return {
            query: {
                phoneNum: '',
                userName: '',
                pageIndex: 1,
                pageSize: 10,
                userIdCreate: null
            },
            tableData: [],
            userData: [],
            patientData: [],
            multipleSelection: [],
            delList: [],
            serviceName: [],
            rootName: [],
            editVisible: false,
            addVisible: false,
            addUserVisible: false,
            editUserVisible: false,
            passwordVisible: false,
            userPasswordVisible: false,
            patientVisible: false,
            priceVisible: false,
            userVisible: false,
            sysUserVisible: false,
            updateExpressVisible: false,
            showExpressVisible: false,
            expressVisible: false,
            editPatientVisible: false,
            editPatientRemarkVisible: false,
            pdfVisible: false,
            reportPdfVisible: false,
            patientTotalRecords: 0,
            totalRecords: 0,
            userTotalRecords: 0,
            accountType: -1,
            form: {},
            idx: -1,
            id: -1,
            filePath: "",
            reportPath: "",
            priceForm: {}
        };
    },
    created() {
        this.getData();
        this.getUserData();
        this.getPatientData();
    },
    methods: {
        // 获取 easy-mock 的模拟数据
        getData() {
            fetchSysuserData(this.query).then(res => {
                this.tableData = res.results;
                this.totalRecords = res.totalRecords || 0;
            });
        },
        getUserData() {
            fetchData(this.query).then(res => {
                this.userData = res.results;
                this.userTotalRecords = res.totalRecords || 0;
            });
        },
        getPatientData() {
            if(this.query.userName != null && this.query.userName != "" && this.query.userName.indexOf("full_") == -1){
                this.query.userName = "full_" + this.query.userName;
            }
            fetchPatientData(this.query).then(res => {
                this.patientData = res.results;
                this.patientTotalRecords = res.totalRecords || 0;
            });
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
                    var patientParam = {userId:this.form.userId};
                    const deleteResult = query => {
                        return request({
                            url: '/api/sysuser/delete',
                            method: 'delete',
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

        // 禁用、启用操作
        handleStatu(index, row) {
            // 二次确认删除
            this.$confirm('确定要更改状态吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    this.form = row;
                    var patientParam = {userId:this.form.userId,status:this.form.status};
                    const deleteResult = query => {
                        return request({
                            url: '/api/sysuser/status',
                            method: 'post',
                            params: patientParam
                        });
                    };

                    deleteResult(this.form).then(res => {
                        var code = res.code;
                        var errorMessage = res.errorMessage;
                        if(code == 200){
                            this.$message.success('更新成功');
                            this.getData();
                        }else{
                            this.$message.error(errorMessage);
                        }
                    });

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
                    str += this.multipleSelection[i].userId + ',';
                }else{
                    str += this.multipleSelection[i].userId ;
                }
            }
           var patient = {sysUserId:str};
            const batchdeleteResult = query => {
                return request({
                    url: '/api/sysuser/batchdelete',
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
                    this.multipleSelection = [];
                    this.getData();
                }else{
                    this.$message.error(errorMessage);
                }
            });
        },

        // 查看详情
        handledetail(path) {
            this.filePath =window.location.protocol + "//" + window.location.host + path;
            this.pdfVisible = true;
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
                        this.getPatientData();
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
                    this.getPatientData();
                }else{
                    this.$message.error(errorMessage);
                }
            });
        },
        showExpress(index, row){
            this.idx = index;
            this.form = row;
            this.updateExpressVisible = true;
            this.getPatientData();
        },
        onlyShowExpress(index, row){
            this.idx = index;
            this.form = row;
            this.showExpressVisible = true;
            this.getPatientData();
        },
        // 编辑操作
        handleEdit(index, row) {
            const addResult = query => {
                return request({
                    url: '/api/sysuser/getAccountType',
                    method: 'get',
                    params: query
                });
            };
            addResult(this.form).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.accountType = res.data;
                }else{
                    this.$message.error(errorMessage);
                }
            });
            const queryResult = query => {
                return request({
                    url: '/api/sysuser/allrootname',
                    method: 'get',
                    params: query
                });
            };
            queryResult(this.form).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.rootName = res.data;
                }else{
                    this.$message.error(errorMessage);
                }
            });
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
                    url: '/api/sysuser/update',
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
        handleUser(index, row){
            this.query.sysUserName = row.userName;
            this.userVisible = true;
            this.getUserData();
        },
        handleDbClickUser(row){
            if(row.accountType == 5 && hasPerm('sysuser:usersUnderName')){
                this.query.sysUserName = row.userName;
                this.userVisible = true;
                this.getUserData();
            }
        },
        // 重置密码操作
        handlePasswordEdit(index, row) {
            this.idx = index;
            this.form = row;
            this.passwordVisible = true;
        },

        // 保存密码
        savePass() {
            this.passwordVisible = false;
            const updateResult = query => {
                if(query.password != null && query.password != ''){
                    query.password = this.$md5(query.password);
                }
                return request({
                    url: '/api/sysuser/updatePass',
                    method: 'post',
                    params: query
                });
            };

            updateResult(this.form).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.$message.success(`重置密码成功`);
                    this.$set(this.tableData, this.idx, this.form);
                }else{
                    this.$message.error(errorMessage);
                }
            });
        },
        // 触发新增按钮
        handleAdd(){
            this.form = {};
            const addResult = query => {
                return request({
                    url: '/api/sysuser/getAccountType',
                    method: 'get',
                    params: query
                });
            };
            addResult(this.form).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.accountType = res.data;
                }else{
                    this.$message.error(errorMessage);
                }
            });
            const queryResult = query => {
                return request({
                    url: '/api/sysuser/allrootname',
                    method: 'get',
                    params: query
                });
            };
            queryResult(this.form).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.rootName = res.data;
                }else{
                    this.$message.error(errorMessage);
                }
            });
            this.addVisible = true;
        },
        // 保存新增
        saveAdd() {
            this.addVisible = false;
            const addResult = query => {
                if(query.password != null && query.password != ''){
                    query.password = this.$md5(query.password);
                }
                return request({
                    url: '/api/sysuser/add',
                    method: 'post',
                    params: query
                });
            };
            addResult(this.form).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.$message.success(`新增成功`);
                    this.getData();
                }else{
                    this.$message.error(errorMessage);
                }
            });
        },
        // 分页导航
        handlePageChange(val) {
            this.$set(this.query, 'pageIndex', val);
            this.getData();
        },
        handleUserColse(){
            this.query.userName = "";
            this.query.phoneNum = "";
        },
        getImgUrl(headPortraitPath){
            return window.location.protocol + "//" + window.location.host + "/" + headPortraitPath;
        },
        // 批量删除User
        delAllUserSelection() {
            const length = this.multipleSelection.length;
            let str = '';
            this.delList = this.delList.concat(this.multipleSelection);
            for (let i = 0; i < length; i++) {
                if(i != (length - 1)){
                    str += this.multipleSelection[i].userId + ',';
                }else{
                    str += this.multipleSelection[i].userId ;
                }
            }
            var user = {userid:str};
            const batchdeleteResult = query => {
                return request({
                    url: '/api/user/batchdelete',
                    method: 'delete',
                    headers: {'Content-Type': 'application/json;charset=UTF-8'},
                    params: user
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
            this.handleUserSearch();
        },
        // 触发User搜索按钮
        handleUserSearch() {
            this.$set(this.query, 'pageIndex', 1);
            this.getUserData();
        },
        // 触发User新增按钮
        handleUserAdd(){
            this.form = {};
            const queryResult = query => {
                return request({
                    url: '/api/sysuser/allname',
                    method: 'get',
                    params: query
                });
            };
            queryResult(this.form).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.serviceName = res.data;
                }else{
                    this.$message.error(errorMessage);
                }
            });
            this.addUserVisible = true;
        },
        // 保存User新增
        saveUserAdd() {
            this.addUserVisible = false;
            const addResult = query => {
                if(query.password != null && query.password != ''){
                    query.password = this.$md5(query.password);
                }
                return request({
                    url: '/api/user/add',
                    method: 'post',
                    params: query
                });
            };
            addResult(this.form).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.$message.success(`新增成功`);
                    this.getData();
                }else{
                    this.$message.error(errorMessage);
                }
            });
            this.handleUserSearch();
        },
        // 编辑操作
        handleUserEdit(index, row) {
            const queryResult = query => {
                return request({
                    url: '/api/sysuser/allname',
                    method: 'get',
                    params: query
                });
            };
            queryResult(this.form).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.serviceName = res.data;
                }else{
                    this.$message.error(errorMessage);
                }
            });
            this.idx = index;
            this.form = row;
            /*this.form.privilege = this.form.privilege + '';*/
            this.editUserVisible = true;
        },
        // 保存User编辑
        saveUserEdit() {
            this.editUserVisible = false;
            const updateResult = query => {
                return request({
                    url: '/api/user/update',
                    method: 'post',
                    params: query
                });
            };
            this.form.userPrices = null;
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
        // User删除操作
        handleUserDelete(index, row) {
            // 二次确认删除
            this.$confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    this.form = row;
                    this.form.userPrices = null;
                    const deleteResult = query => {
                        return request({
                            url: '/api/user/delete',
                            method: 'delete',
                            params: this.form
                        });
                    };
                    deleteResult(this.form).then(res => {
                        var code = res.code;
                        var errorMessage = res.errorMessage;
                        if(code == 200){
                            this.handleUserSearch();
                            this.$message.success('删除成功');
                        }else{
                            this.$message.error(errorMessage);
                        }
                    });
                })
                .cancel(() => {})
                .catch(() => { this.$message.error("哎呀，出错啦！");});
        },
        // User重置密码操作
        handleUserPasswordEdit(index, row) {
            this.idx = index;
            this.form = row;
            this.userPasswordVisible = true;
        },
        // User保存密码
        saveUserPass() {
            this.userPasswordVisible = false;
            const updateResult = query => {
                if(query.password != null && query.password != ''){
                    query.password = this.$md5(query.password);
                }
                return request({
                    url: '/api/user/updatePassword',
                    method: 'post',
                    params: query
                });
            };
            this.form.userPrices = null;
            updateResult(this.form).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.$message.success(`重置密码成功`);
                    this.$set(this.tableData, this.idx, this.form);
                }else{
                    this.$message.error(errorMessage);
                }
            });
        },
        // 设置价格
        handlePrice(index, row) {
            this.editPatientVisible = false;
            const saveResult = query => {
                return request({
                    url: '/api/price/all',
                    method: 'get',
                    params: query,
                });
            };
            var param = {"userId":row.userId};
            saveResult(param).then(res => {
                this.priceForm = res;
            });
            this.idx = index;
            /*this.priceForm = row;*/
            this.priceVisible = true;
        },
        //保存价格信息
        savePriceEdit(){
            this.priceVisible = false;
            const updateResult = query => {
                return request({
                    url: '/api/price/update',
                    method: 'post',
                    params: query,
                });
            };
            /* var param = {"userPrice":JSON.stringify(this.priceForm.results)};*/
            updateResult(JSON.stringify(this.priceForm.results)).then(res => {
                var code = res.code;
                var errorMessage = res.errorMessage;
                if(code == 200){
                    this.$message.success(`修改成功`);
                }else{
                    this.$message.error(errorMessage);
                }
            });
        },

        handleSysUser(index, row){
            this.query.userIdCreate = row.userId;
            this.query.accountType = -1;
            this.sysUserVisible = true;
            this.getData();
        },
        handleSysUserColse(){
            this.query.userName = "";
            this.query.phoneNum = "";
            this.query.userIdCreate = null;
            this.getData();
        },
        handlePatient(index, row){
            this.query.userName = row.userName;
            //this.query.sysUserName = null;
            this.patientVisible = true;
            this.getPatientData();
        },
        handlePatientColse(){
            this.query.userName = "";
            this.query.phoneNum = "";
        },
        // 编辑操作
        handlePatientEdit(index, row) {
            this.idx = index;
            this.form = row;
            this.editPatientVisible = true;
            this.getPatientData();
        },
        // 编辑备注操作
        handlePatientEditRemark(index, row) {
            this.idx = index;
            this.form = row;
            this.editPatientRemarkVisible = true;
            this.getPatientData();
        },
        // 保存编辑
        savePatientEdit() {
            this.editPatientVisible = false;
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
                    this.$set(this.patientData, this.idx, this.form);
                }else{
                    this.$message.error(errorMessage);
                }
            });
        },

        // 保存编辑
        savePatientRemarkEdit() {
            this.editPatientRemarkVisible = false;
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
                    this.$set(this.patientData, this.idx, this.form);
                }else{
                    this.$message.error(errorMessage);
                }
            });
        },
        // 删除操作
        handlePatientDelete(index, row) {
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

                    this.patientData.splice(index, 1);
                })
                .cancel(() => {})
                .catch(() => { this.$message.error("哎呀，出错啦！");});
        },
        // 查看结果
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
        delAllPatientSelection() {
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
            this.handlePatientSearch();
        },
        handlePatientPageChange(val) {
            this.$set(this.query, 'pageIndex', val);
            this.getPatientData();
        },
        getImgUrl(headPortraitPath){
            return window.location.protocol + "//" + window.location.host + "/" + headPortraitPath;
        },
        changeTableSort(column){
            if (column.order === 'descending') {
                this.query.sortby = column.prop
                this.query.orderBy = 'DESC'
            } else {
                this.query.sortby = column.prop
                this.query.orderBy = 'ASC'
            }
            this.getPatientData();
        },
        exportExcel () {
            /* this.handleExportSearch();*/
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
        // 触发搜索按钮
        handlePatientSearch() {
            this.$set(this.query, 'pageIndex', 1);
            this.getPatientData();
        },
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
