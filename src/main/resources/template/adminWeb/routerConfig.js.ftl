import asyncComponent from '@components/AsyncComponent'

const moduleRouterConfig = [
  {
    path: '/demo',
    component: asyncComponent(()=>import('@modules/demo')),
  },
<#list tableColumnList as item>
  {
    path: '/${item.businessEnName}',
    component: asyncComponent(()=>import('@modules/${item.businessEnName}'))
  },
</#list>
];

export default moduleRouterConfig;
