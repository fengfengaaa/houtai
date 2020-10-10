import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import VueI18n from 'vue-i18n';
import { messages } from './components/common/i18n';
import md5 from 'js-md5';
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
// import './assets/css/theme-green/index.css'; // 浅绿色主题
import './assets/css/icon.css';
import './components/common/directives';
import 'babel-polyfill';

Vue.prototype.$md5 = md5
Vue.config.productionTip = false;
Vue.use(VueI18n);
Vue.use(ElementUI, {
    size: 'small'
});
const i18n = new VueI18n({
    locale: 'zh',
    messages
});

//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title} | UAK足姿评估系统`;
    var roles = localStorage.getItem('routperms');
    var isadmin = localStorage.getItem('isadmin');
    var username = localStorage.getItem('ms_username');
    if(username == null){
        username = 'unknow';
    }
    if(isadmin == null){
        isadmin = false;
    }
    if(roles == null){
        roles = ["/dashboard"];
    }
    if (!username && to.path !== '/login') {
        alert('登陆失效，请稍后重试');
    } else if (to.meta.permission) {
        if(isadmin != "true"){
            let getBtnPermissionArr = JSON.parse(roles);
            var contains = getBtnPermissionArr.indexOf(to.path) > -1;
            if(!contains){
                next('/403');
            }else{
                next();
            }
          /*  role === 'admin' ? next() : next('/403');*/
        }else{
            next();
        }
    } else {
        // 简单的判断IE10及以下不进入富文本编辑器，该组件不兼容
        if (navigator.userAgent.indexOf('MSIE') > -1 && to.path === '/editor') {
            Vue.prototype.$alert('vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看', '浏览器不兼容通知', {
                confirmButtonText: '确定'
            });
        } else {
            next();
        }
    }
});

function hasBtnPermission(permission){
    var isadmin = localStorage.getItem('isadmin');
    if(isadmin == "true"){
        return true;
    }
    let getBtnPermissionArr = JSON.parse(localStorage.getItem('menuperms'));
    // console.log(permission);
    // console.log(getBtnPermissionArr )
    // console.log(getBtnPermissionArr .indexOf(permission) > -1)
    return getBtnPermissionArr.indexOf(permission) > -1;
}
Vue.prototype.hasPerm = hasBtnPermission;

new Vue({
    router,
    i18n,
    render: h => h(App)
}).$mount('#app');
