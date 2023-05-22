<template>
	<div>
		<el-header>
			<span class="fontClass" @click="collapse"><i :class="isCollapse?'fas fa-indent fa-lg':'fas fa-outdent fa-lg'"></i></span>
			<el-menu class="user-info" mode="horizontal">
          <el-menu-item index="1">
            <el-link icon="el-icon-position" href="start">发起</el-link>
          </el-menu-item>
          <el-menu-item index="2">
            <el-link icon="el-icon-bell" href="todolist">待办</el-link>
          </el-menu-item>
          <el-menu-item index="3">
            <el-link icon="el-icon-time" href="runing">在途</el-link>
          </el-menu-item>
          <el-submenu index="4">
            <template slot="title">{{ username }}</template>
<!--			          <el-menu-item index="4-1" @click="userTo">个人信息</el-menu-item>-->
            <el-menu-item index="4-1" @click="logout">退出</el-menu-item>
          </el-submenu>
        </el-menu>
		</el-header>
	</div>
</template>

<script>
	import {
		WebUser
	} from "../../api/Gener.js";
	export default {
		name: "common-header",
		data() {
			return {
				logosrc: '../../../public/img/logo.png',
				changePwdVisible: false,
				constRole: "",
				username: "",
				userRole: "",
        isCollapse:false
			};
		},
		components: {},
		methods: {
			logout() {
				this.$confirm("此操作将登出当前用户, 是否继续?", "提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning"
					})
					.then(() => {
						this.sendLoginout();
					})
					.catch(() => {});
			},
			sendLoginout() {
				this.$router.push({
					name: 'login'
				})
			},
			collapse(){
				this.isCollapse = !this.isCollapse;
				this.$emit('on-collapse',this.isCollapse)
			},
			userTo(){
				this.$router.push({
					name: 'Default'
				})
			}
		},
		created() {
			var webUser = new WebUser();
			this.username = webUser.Name;
			this.$store.commit('setWebUser', webUser);

		}
	};
</script>

<style lang="less" scoped>
	.el-header {
		width: 100%;
		height: 60px;
		line-height: 56px;
		background: #ffffff;
		border-bottom: 1px solid #efefef;
		box-sizing: border-box;
		z-index: 3;
		top: 0;
		padding-left:0px ;
		background-color: #eef1f6;
	}

	

	.title {
		float: left;
		margin-left: 20px;
		font-size: 16px;
		font-weight: 500;
		color: #409EFF;
	}

	.user-info {
		float: right;
		font-size: 12px;
		font-weight: 400;
		color: #17243f;
	}
    .tabalink{
		margin-right: 20px;
		line-height: 19px;
		color: #fff;
	}
	.role {
		margin-right: 20px;
		color: #409EFF;
		span {
			margin-left: 10px;
		}
	}

	.name {
		margin: 0 20px;
	}

	.logout {
		margin: 0 20px;
		cursor: pointer;
	}

	.change-pwd {
		cursor: pointer;
	}

	.el-divider--vertical {
		width: 1px;
		height: 24px;
		margin: 0;
		background-color: #d8dfea;
	}
	.fontClass{
		margin-left: 15px;
		color:#666
	}
	.el-menu {
    background-color: transparent;
	}
</style>