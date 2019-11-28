import React, { Component } from 'react';
import { Input, Form, message } from 'antd';
import FixedRow from '@/common/components/FixedRow';
import request from '@/common/utils/request';

class ${businessEnNameUpFirst}Edit extends Component {
  constructor(props) {
    super(props)
    request.get('/${businessEnName}/' + this.props.currentRecord.id).then(res => {
      this.props.form.setFieldsValue(res);
    });
  }

  onOk(modal) {
    this.props.form.validateFields((err, values) => {
      if (!!err) {
        return;
      };
      request.put('/${businessEnName}', { data: values }).then(res => {
        //提交成功
        message.success('保存成功');
        modal.hideAndRefresh();
      });

    });
  }
  render() {
    const { getFieldDecorator } = this.props.form;
    return (
      <Form>
        <#list columnList as item>
        <FixedRow>
          <Form.Item label="${item.columnComment}">
            {
              getFieldDecorator("${item.columnNameHump}", { <#if item.isNullable=='NO'> rules: [{  required: true, message: "请填写${item.columnComment}"  }] </#if> })(<Input />)
            }
          </Form.Item>
        </FixedRow>
        </#list>
      </Form >
    );
  }
}
export default Form.create()(${businessEnNameUpFirst}Edit);
