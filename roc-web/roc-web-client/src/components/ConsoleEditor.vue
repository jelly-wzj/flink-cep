<template>
    <el-container>
        <el-header class="console_header">
            <span style="margin-left: 10px">模板引擎： </span>
            <el-select
                    v-model="cmEditorMode"
                    placeholder="请选择模板引擎"
                    size="small"
                    style="width:150px"
                    @change="onEditorModeChange"
            >
                <el-option
                        v-for="item in cmEditorModeOptions"
                        :key="item"
                        :label="item"
                        :value="item"
                ></el-option>
            </el-select>
        </el-header>
        <el-main class="console_main">
            <code-mirror-editor
                    ref="cmEditor"
                    :cmTheme="cmTheme"
                    :cmMode="cmMode"
                    :autoFormatJson="autoFormatJson"
                    :jsonIndentation="jsonIndentation"
            ></code-mirror-editor>
        </el-main>
        <el-footer>
            <el-button type="primary" size="medium" style="margin-top: 10px" @click="setValue">重置</el-button>
            <el-button type="primary" size="medium" style="margin-left: 10px" @click="getValue">提交作业</el-button>
        </el-footer>
    </el-container>
</template>
<script>
    import CodeMirrorEditor from "@/components/public/CodeMirrorEditor";

    export default {
        components: {
            CodeMirrorEditor
        },
        data() {
            return {
                cmTheme: "eclipse",
                cmEditorMode: "sql",
                cmEditorModeOptions: [
                    "json",
                    "sql",
                    "java",
                    "groovy",
                    "file"
                ],
                // codeMirror模式
                cmMode: "application/json",
                // json编辑模式下，json格式化缩进 支持字符或数字，最大不超过10，默认缩进2个空格
                jsonIndentation: 2,
                // json编辑模式下，输入框失去焦点时是否自动格式化，true 开启， false 关闭
                autoFormatJson: true
            };
        },
        methods: {
            // 切换编辑模式事件处理函数
            onEditorModeChange(value) {
                switch (value) {
                    case "json":
                        this.cmMode = "application/json";
                        break;
                    case "sql":
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
            //获取内容
            getValue() {
                let content = this.$refs.cmEditor.getValue();
                console.log(content);
            },
            //重置
            setValue() {
                this.$refs.cmEditor.setEmpty();
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
    }
</style>