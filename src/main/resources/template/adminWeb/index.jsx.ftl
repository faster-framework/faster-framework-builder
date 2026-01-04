import React, { Component } from 'react';
import { Input, Button, message, Modal } from 'antd';
import { GridContent } from '@ant-design/pro-layout';
import ModalInfo from '@/common/components/Modal';
import TableList, { Search, Table, Action } from '@/common/components/TableList';
import request from '@/common/utils/request'
import Permission from '@/common/components/Permission';

import ${businessEnNameUpFirst}Add from './${businessEnNameUpFirst}Add';
import ${businessEnNameUpFirst}Edit from './${businessEnNameUpFirst}Edit';

export default class ${businessEnNameUpFirst}List extends Component {

  constructor(props) {
    super(props);
    // 默认筛选参数，key与下方筛选条件内的name相对应
    this.defaultParam = {
    }
  }
  /**
   * 渲染操作列
   * text: 当前行的值
   * record: 当前行数据
   * index: 行索引
   */
  renderColAction = (text, record, index) => {
    return (
      <>
        <Permission authority="${businessEnName}:modify">
          <a onClick={() => this.refs.editModal.show(record)}>修改</a>
        </Permission>
        <Permission authority="${businessEnName}:delete">
          <a onClick={() => this.delete(record)}>删除</a>
        </Permission>
      </>
    );
  }
  /**
   * 删除
   */
  delete = (record) => {
    const tableList = this.refs.tableList;
    const selectRecrods = tableList.currentSelectRows(record);
    if (selectRecrods.length == 0) {
      message.error('请选择至少一条记录！');
      return;
    }
    Modal.confirm({
      title: '删除',
      okText: "确认",
      cancelText: "取消",
      centered: true,
      content: '删除操作不可恢复，确认删除？',
      onOk() {
        return new Promise((resolve, reject) => {
          request.delete("/${businessEnName}/delete", { data: selectRecrods.map(item => item.id) } ).then(res => {
            resolve();
            tableList.reload();
          }).catch(() => reject());
        });
      }
    });
  }
  render() {
    return (
      <GridContent>
        <TableList ref='tableList'>
          <Search>
          <#list columnList as item>
            <Input label='${item.columnComment}' name='${item.columnNameHump}' />
          </#list>
          </Search>
          <Action>
            <Permission authority="${businessEnName}:add">
              <Button type="primary" icon="plus" onClick={() => this.refs.addModal.show()}>添加</Button>
            </Permission>
            <Permission authority="${businessEnName}:delete">
              <Button type="primary" icon="delete" onClick={() => this.delete()}>删除</Button>
            </Permission>
          </Action>
          <Table url='/${businessEnName}' defaultParam={this.defaultParam}>
          <#list columnList as item>
            <Table.Column title="${item.columnComment}" dataIndex="${item.columnNameHump}" />
          </#list>
            <Table.Action render={this.renderColAction}></Table.Action>
          </Table>
        </TableList >
        <ModalInfo title='添加${businessCnName}' ref="addModal" {...this.refs}>
          <${businessEnNameUpFirst}Add />
        </ModalInfo>
        <ModalInfo title='编辑${businessCnName}' ref="editModal" {...this.refs}>
          <${businessEnNameUpFirst}Edit />
        </ModalInfo>
      </GridContent >
    );
  }
}