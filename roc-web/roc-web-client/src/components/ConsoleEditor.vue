<template>
    <el-container>
        <el-header class="console_header">
            <span style="margin-left: 10px">模板引擎： </span>
            <el-select
                    v-model="cmEditorMode"
                    placeholder="请选择模板引擎"
                    size="small"
                    style="width:150px"
                    @change="onEditorModeChange">
                <el-option
                        v-for="item in cmEditorModeOptions"
                        :key="item"
                        :label="item"
                        :value="item"></el-option>
            </el-select>
        </el-header>
        <el-container>
            <el-main class="console_main">
                <code-mirror-editor
                        ref="cmEditor"
                        :cmTheme="cmTheme"
                        :cmMode="cmMode"
                        :autoFormatJson="autoFormatJson"
                        :jsonIndentation="jsonIndentation"></code-mirror-editor>
            </el-main>
            <el-aside class="console_side">
                <el-main class="source_main">
                    <span>输入源:  </span>
                    <el-form ref="sources">
                        <el-select v-model="sourcesStorageType"
                                   placeholder="Type"
                                   size="small"
                                   style="width: 250px"
                                   @change="onSourcesTypeChange">
                            <el-option v-for="item in sourcesStorageTypeOptions"
                                       :key="item"
                                       :label="item"
                                       :value="item"/>
                        </el-select>
                        <el-input v-model="sources_host" placeholder="Host" size="small" style="width: 250px"/>
                        <el-input v-model="sources_fields" placeholder="Fields" size="small" style="width: 250px"/>
                        <el-input v-model="sources_storage" placeholder="Storage" size="small" style="width: 250px"/>
                        <el-input v-model="sources_auth" placeholder="Auth" size="small" style="width: 250px"/>
                    </el-form>
                </el-main>
                <el-main class="source_main">
                    <span>输出源: </span>
                    <el-form ref="sinks">
                        <el-select v-model="sinksStorageType"
                                   placeholder="Type"
                                   size="small"
                                   style="width: 250px"
                                   @change="onSinkTypeChange">
                            <el-option v-for="item in sinksStorageTypeOptions"
                                       :key="item"
                                       :label="item"
                                       :value="item"/>
                        </el-select>
                        <el-input v-model="sinks_host" placeholder="Host" size="small" style="width: 250px"/>
                        <el-input v-model="sinks_fields" placeholder="Fields" size="small" style="width: 250px"/>
                        <el-input v-model="sinks_storage" placeholder="Storage" size="small" style="width: 250px"/>
                        <el-input v-model="sinks_auth" placeholder="Auth" size="small" style="width: 250px"/>
                    </el-form>
                </el-main>
            </el-aside>
        </el-container>
        <el-footer>
            <el-button type="primary" size="medium" style="margin-top: 10px" @click="reset">重置</el-button>
            <el-button type="primary" size="medium" style="margin-left: 10px" @click="submit">提交作业</el-button>
        </el-footer>
    </el-container>
</template>
<script>
    import CodeMirrorEditor from "@/components/public/CodeMirrorEditor";
    import {post} from '../utils/api'

    export default {
        components: {
            CodeMirrorEditor
        },
        data() {
            return {
                cmTheme: "eclipse",
                cmEditorMode: "",
                cmEditorModeOptions: [
                    "json",
                    "cql",
                    "java",
                    "groovy",
                    "file"
                ],
                // codeMirror模式
                cmMode: "application/json",
                // json编辑模式下，json格式化缩进 支持字符或数字，最大不超过10，默认缩进2个空格
                jsonIndentation: 2,
                // json编辑模式下，输入框失去焦点时是否自动格式化，true 开启， false 关闭
                autoFormatJson: true,
                sourcesStorageType: "kafka",
                sourcesStorageTypeOptions: [
                    "kafka",
                    "hbase",
                    "mysql",
                    "elasticsearch"
                ],
                sinksStorageType: "kafka",
                sinksStorageTypeOptions: [
                    "kafka",
                    "hbase",
                    "mysql",
                    "elasticsearch",
                    "redis"
                ],
                sources_host: "",
                sources_fields: "",
                sources_storage: "",
                sources_auth: "",
                sinks_host: "",
                sinks_fields: "",
                sinks_storage: "",
                sinks_auth: ""
            };
        },
        methods: {
            // 切换编辑模式事件处理函数
            onEditorModeChange(value) {
                switch (value) {
                    case "json":
                        this.cmMode = "application/json";
                        break;
                    case "cql":
                        this.cmMode = "sql";
                        break;
                    case "java":
                        this.cmMode = "java";
                        break;
                    case "script":
                        this.cmMode = "groovy";
                        break;
                    case "file":
                        this.cmMode = "file";
                        break;
                    default:
                        this.cmMode = "application/json";
                }
            },
            onSourcesTypeChange(value) {
                switch (value) {
                    case "kafka":
                        this.sourcesStorageType = "kafka";
                        break;
                    case "hbase":
                        this.sourcesStorageType = "hbase";
                        break;
                    case "mysql":
                        this.sourcesStorageType = "mysql";
                        break;
                    case "elasticsearch":
                        this.sourcesStorageType = "elasticsearch";
                        break;
                    default:
                        this.sourcesStorageType = "kafka"
                }
            },
            onSinkTypeChange(value) {
                switch (value) {
                    case "kafka":
                        this.sinksStorageType = "kafka";
                        break;
                    case "hbase":
                        this.sinksStorageType = "hbase";
                        break;
                    case "mysql":
                        this.sinksStorageType = "mysql";
                        break;
                    case "elasticsearch":
                        this.sinksStorageType = "elasticsearch";
                        break;
                    case "redis":
                        this.sinksStorageType = "redis";
                        break;
                    default:
                        this.sinksStorageType = "kafka"
                }
            },
            //重置
            reset() {
                this.$refs.cmEditor.setEmpty();
            },
            // 提交任务
            submit() {
                let exp = this.$refs.cmEditor.getValue();

                let submitData = {
                    "streamEngine": this.cmMode,
                    "exp": this.$refs.cmEditor.getValue(),
                    "sources": [
                        {
                            "fields": this.sources_fields,
                            "type": this.sourcesStorageType,
                            "host": this.sources_host,
                            "auth": this.sources_auth,
                            "storage": this.sources_storage
                        }
                    ],
                    "sinks": [
                        {
                            "type": this.sinksStorageType,
                            "host": this.sinks_host,
                            "auth": this.sinks_auth,
                            "storage": this.sinks_storage
                        }
                    ]
                }
                // 提交请求
                post('/admin/task', submitData).then(resp => {
                    if (resp.status == 200) {
                        this.$message({
                            type: resp.data.status,
                            message: resp.data.msg
                        })
                    } else {
                        this.$message({
                            type: 'error',
                            message: '任务提交失败 -): ' + resp.status
                        })
                    }
                }).catch(Error => {
                    console.log(Error)
                    this.$message({
                        type: 'error',
                        message: '任务提交失败 -): ' + Error.message
                    })
                })
            }
        }
    };
</script>

<style>
    .CodeMirror {
        position: static;
        height: 420px;
        text-align: left;
    }

    .console_header {
        background-color: #ececec;
        margin-top: 20px;
        padding-left: 5px;
        display: flex;
        justify-content: flex-start;
    }

    .console_main {
        justify-content: flex-start;
        display: flex;
        flex-direction: column;
        padding-left: 5px;
        background-color: #ececec;
        margin-top: 10px;
        padding-top: 10px;
        height: 450px;
        width: 50%;
    }

    .console_side {
        padding-left: 5px;
        background-color: #ececec;
        margin-top: 10px;
        padding-top: 10px;
    }

    .source_main {
        padding-right: 200px;
        background-color: #ececec;
    }
</style>