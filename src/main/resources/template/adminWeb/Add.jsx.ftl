import React, { Component } from 'react';
import { Input, Grid, Form, Button, Field } from '@icedesign/base';
import { http } from '@utils';


const { Row, Col } = Grid;
const FormItem = Form.Item;

export default class ${businessEnNameUpFirst}Add extends Component {
    static displayName = '${businessEnNameUpFirst}Add';
    field = new Field(this, {
        deepReset: true // 打开清除特殊类型模式(fileList是数组需要特别开启)
    });
    constructor(props) {
        super(props);
    }

    save = () => {
        this.field.validate((errors, values) => {
            if (errors) {
                return false;
            }
            http.post('/${businessEnName}', values).then(() => {
                this.props.addDialog.hide();
                this.props.tableList.refresh();
            });
        });
    };

    render() {
        const formItemLayout = {
            labelCol: { fixedSpan: 6 },
            wrapperCol: { fixedSpan: 8 },
            style: {
                marginRight: '10px'
            }
        };
        const { init } = this.field;
        return (
            <Form field={this.field}>
                <#list columnList as item>
                <Row wrap>
                    <FormItem {...formItemLayout} label=" ${item.columnComment}：">
                        <Input placeholder="请输入${item.columnNameHump}" {...init("${item.columnNameHump}", { <#if item.isNullable=='NO'> rules: { required: true },</#if> message: "请填写${item.columnComment}" })} />
                    </FormItem>
                </Row>
                </#list>
                <Row wrap>
                    <Col style={{ textAlign: "center" }}>
                        <Button type="primary" style={formItemLayout.style} onClick={this.save}>保存</Button>
                        <Button onClick={() => this.props.addDialog.hide()}>取消</Button>
                    </Col>
                </Row>
            </Form>
        );
    }
}
