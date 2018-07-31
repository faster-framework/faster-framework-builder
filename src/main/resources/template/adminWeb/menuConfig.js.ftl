const moduleMenuConfig = [
    {
        name: 'demo',
        path: '/demo',
        icon: 'home',
    },
<#list tableColumnList as item>
    {
        name: '${item.businessCnName}管理',
        path: '/${item.businessEnName}',
        icon: 'home',
        code: '${item.businessEnName}:manage'
    },
</#list>
];
export default moduleMenuConfig;