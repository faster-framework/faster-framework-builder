import React, { Component } from 'react';
import { Table, Icon, Grid, Button, Input, Feedback } from '@icedesign/base';
import TableList from '@components/TableList';
import { FormBinder } from '@icedesign/form-binder';
import Dialog from '@components/Dialog';
import { http } from '@utils';
import Permission from '@components/Permission';

import ${businessEnNameUpFirst}Add from './${businessEnNameUpFirst}Add';
import ${businessEnNameUpFirst}Edit from './${businessEnNameUpFirst}Edit';

const { Col } = Grid;
export default class ${businessEnNameUpFirst}List extends Component {
  static displayName = '${businessEnNameUpFirst}List';

  constructor(props) {
    super(props);
    // 默认筛选参数，key与下方筛选条件内的name相对应
    this.defaultFilterParam = {
    }
  }

  /**
   * 选择验证
   */
  checkAndShow = (dialog) => {
    const selectRecrod = this.refs.tableList.getSelectRecords();
    if (selectRecrod.length == 1) {
      dialog.show();
    } else {
      Feedback.toast.error('请选择一条记录！');
    }
  }

  /**
   * 删除
   */
  delete = () => {
    const selectRecrod = this.refs.tableList.getSelectRecords();
    http.delete("/${businessEnName}/" + selectRecrod[0].id).then(() => {
      this.refs.deleteDialog.hide();
      this.refs.tableList.refresh();
    });
  }
  /**
   * 渲染操作列
   */
  renderOper = (value, index, record) => {
    return (
      <div className="col-operation">
        <Permission code="${businessEnName}:modify">
          <Button shape="text" size="small" onClick={() => this.refs.editDialog.show()}>
            修改
          </Button>
        </Permission>
      </div>
    );
  };

  render() {
    return (
      <div>
        {/* 表格开始 */}
        <TableList ref="tableList" api='/${businessEnName}' title="${businessCnName}列表" defaultFilterParam={this.defaultFilterParam}>
          {/* 筛选开始 */}
          <div key="filters">
          <#list columnList as item>
            <Col xxs="24" m="12" l="8" >
              <span>${item.columnComment}:</span>
              <FormBinder name="${item.columnNameHump}">
                <Input />
              </FormBinder>
            </Col>
          </#list>
          </div>
          {/* 筛选结束 */}

          {/* 操作开始 */}
          <div key="operations">
            <Permission code="${businessEnName}:add">
              <Button type="primary" onClick={() => this.refs.addDialog.show()}>
                <Icon type="add" size="xs" />添加
                </Button>
            </Permission>
            <Permission code="${businessEnName}:delete">
              <Button type="primary" onClick={() => this.checkAndShow(this.refs.deleteDialog)}>
                <Icon type="close" size="xs" />删除
                </Button>
            </Permission>
          </div>
          {/* 操作结束 */}

          {/* 列表开始  */}
          <div key="tables">
          <#list columnList as item>
            <Table.Column title="${item.columnComment}" dataIndex="${item.columnNameHump}" />
          </#list>
            <Table.Column title="操作" cell={this.renderOper} />
          </div>
          {/* 列表结束 */}
        </TableList>
        {/* 表格结束 */}

        {/* 弹框开始 */}

        <Dialog
          title="添加${businessCnName}"
          ref="addDialog"
          footer={false}
        >
          <${businessEnNameUpFirst}Add {...this.refs}></${businessEnNameUpFirst}Add>
        </Dialog>
        <Dialog
          title="编辑${businessCnName}"
          ref="editDialog"
          footer={false}
        >
          <${businessEnNameUpFirst}Edit {...this.refs}></${businessEnNameUpFirst}Edit>
        </Dialog>
        <Dialog
          title="删除${businessCnName}"
          ref="deleteDialog"
          locale={{ ok: '确认' }}
          onOk={this.delete}
        >
          删除操作不可恢复，确认删除？
        </Dialog>
        {/* 弹框结束 */}

      </div>
    );
  }
}