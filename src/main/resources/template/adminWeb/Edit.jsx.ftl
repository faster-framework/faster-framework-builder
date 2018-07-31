import React, { Component } from 'react';
import { Input, Grid, Form, Button, Field } from '@icedesign/base';
import { http } from '@utils';


const { Row, Col } = Grid;
const FormItem = Form.Item;

export default class ${businessEnNameUpFirst}Edit extends Component {
    static displayName = '${businessEnNameUpFirst}Edit';
    field = new Field(this, {
        deepReset: true // 打开清除特殊类型模式(fileList是数组需要特别开启)
    });
    constructor(props) {
        super(props);
        const selectRecord = this.props.tableList.getSelectRecords()[0];
        http.get('/${businessEnName}/' + selectRecord.id).then(response => {
            this.field.setValues(response.data);
        })
    }

    save = () => {
        this.field.validate((errors, values) => {
            if (errors) {
                return false;
            }
            http.put('/${businessEnName}/' + values.id, values).then(() => {
                this.props.editDialog.hide();
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
                        <Input placeholder="请输入${item.columnNameHump}" {...init("${item.columnNameHump}", { rules: { required: <#if item.isNullable=='NO'>true<#else>false</#if>}, message: "请填写${item.columnComment}" })} />
                    </FormItem>
                </Row>
                </#list>
                <Row wrap>
                    <Col style={{ textAlign: "center" }}>
                        <Button type="primary" style={formItemLayout.style} onClick={this.save}>保存</Button>
                        <Button onClick={() => this.props.editDialog.hide()}>取消</Button>
                    </Col>
                </Row>
            </Form>
        );
    }
}
