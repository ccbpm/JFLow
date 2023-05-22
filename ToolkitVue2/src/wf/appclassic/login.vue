<template>
	<el-container class="login-bg">
		<el-row class="login-container" type="flex" align="middle">

			<el-col :span='12' class="login-left">
				<div class="logo">
					<h2>经典的、永恒的、奔腾不息的驰骋BPM...</h2>
				</div>
				<div class="left-box">
					<el-image style="width:60%" :src="require('../../../public/img/ccbpm.png')"></el-image>
					<h2>工作流\表单中间件-低代码平台</h2>
					<ul>
						<li>技术选型：vue2.0 </li>
						<li>官网:<a href="http://ccflow.org?frm=client" target="_blank"> http://ccflow.org</a></li>
						<li>地址：济南市.高新区.碧桂园凤凰国际A座F19</li>
						<li>电话：0531-82374939,18660153393(微信)</li>
						<li>版权：济南驰骋信息技术有限公司 @2003-2022</li>

						<li><a href="http://help.jflow.cn:8081/" target="_blank"> 流程设计器登录</a></li>

					</ul>
				</div>
			</el-col>
			<el-col :span='12' class="login-right">
				<el-form :model="ruleForm2" :rules="rules2" status-icon ref="ruleForm2" label-position="left"
					label-width="0px" class="demo-ruleForm login-page my-auto">
					<h3 class="title ">用户端登录 - Vue</h3>
					<el-form-item prop="username">
						<el-input type="text" v-model="ruleForm2.username" auto-complete="off"
							placeholder="用户名，管理员admin"></el-input>
					</el-form-item>
					<el-form-item prop="password">
						<el-input type="password" v-model="ruleForm2.password" auto-complete="off"
							placeholder="密码，默认123"></el-input>
					</el-form-item>
					<el-form-item style="width:100%;">
						<el-button type="primary" style="width:100%;" @click="handleSubmit" :loading="logining">登录 -
							流程中间件 - 用户端</el-button>
					</el-form-item>
					<el-divider content-position="center" class="gray" hidden="hidden">其他登录</el-divider>
					<el-row style="text-align: center;">

						<el-button type="primary" onclick="alert('未实现');" icon="iconfont icon-qq" circle></el-button>
						<el-button type="success" onclick="alert('未实现');" icon="iconfont icon-weixin" circle>
						</el-button>
						<el-button type="danger" onclick="alert('未实现');" icon="iconfont icon-zhifubaozhifu" circle>
						</el-button>
					</el-row>
				</el-form>

			</el-col>
		</el-row>
	</el-container>
</template>

<script>
	export default {
		data() {
			return {
				logining: false,
				show: true,
				ruleForm2: {
					username: 'admin',
					password: '123',
				},
				rules2: {
					username: [{
						required: true,
						message: '请输入用户名',
						trigger: 'blur'
					}],
					password: [{
						required: true,
						message: '请输入密码',
						trigger: 'blur'
					}]
				},
				checked: false
			}
		},
		created() {

			// var baby=new test();
			// this.$router.push("myFlow?FK_Flow=350");

		},
		methods: {
			handleSubmit() {
				var handler = new this.HttpHandler("BP.WF.HttpHandler.WF_AppClassic");
				handler.AddPara("TB_No", this.ruleForm2.username);
				handler.AddPara("TB_PW", this.ruleForm2.password);
				var data = handler.DoMethodReturnString("Login_Submit");
				if (data.includes("err")) {
					this.$message.error('登录失败');
					return;
				}
				this.$router.push({
					path: '/start'
				});
			}
		},

	};
</script>

<style scoped>
	.my-auto {
		margin-top: auto;
		margin-bottom: auto;
	}

	.login-bg {
		min-height: 100%;
	}

	.logo {
		display: flex;
		position: fixed;
		top: 0;
		color: #fff;
	}

	.login-bg:before {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		margin-left: -48%;
		background-image: url(https://vvbin.cn/next/assets/login-bg.b9f5c736.svg);
		background-position: 100%;
		background-repeat: no-repeat;
		background-size: auto 100%;
		content: ""
	}

	.left-box {
		color: #fff;
		width: 70%;
		padding: 2rem;
		margin: 0 auto;
	}

	.title {
		font-size: 2.575rem;
		font-weight: 700;
	}

	.login-container {
		width: 100%;
		padding: 2rem;

	}

	.login-left {
		display: flex;
	}

	.login-right {
		display: flex;
	}

	.login-page {
		width: 70%;
		padding: 2rem;
		margin: 0 auto;


	}

	label.el-checkbox.rememberme {
		margin: 0px 0px 15px;
		text-align: left;
	}

	.el-input__inner,
	.el-button {
		border-radius: 0px;
	}

	.el-divider__text {
		color: #999;
	}
</style>
