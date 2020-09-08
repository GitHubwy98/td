var isShow = true;  //定义一个标志位
$('.kit-side-fold').click(function () {
    //选择出所有的span，并判断是不是hidden
    $('.layui-nav-item span').each(function () {
        if ($(this).is(':hidden')) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
    //判断isshow的状态
    if (isShow) {
        $('.layui-side.layui-bg-black').width(60); //设置宽度
        $('.kit-side-fold i').css('margin-right', '70%');  //修改图标的位置
        //将footer和body的宽度修改
        $('.layui-body').css('left', 60 + 'px');
        $('.layui-footer').css('left', 60 + 'px');
        //将二级导航栏隐藏
        $('dd span').each(function () {
            $(this).hide();
        });
        $('.layui-logo').width(60);
        $('.layui-logo span').hide();
        $('.layui-layout-left').css('left',60+'px');
        $('.layui-nav-child').css("display","none")
        //修改标志位
        isShow = false;
    } else {
        $('.layui-side.layui-bg-black').width(200);
        $('.kit-side-fold i').css('margin-right', '10%');
        $('.layui-body').css('left', 200 + 'px');
        $('.layui-footer').css('left', 200 + 'px');
        $('dd span').each(function () {
            $(this).show();
        });
        $('.layui-logo').width(200);
        $('.layui-logo span').show();
        $('.layui-layout-left').css('left', 200 + 'px');
        $('.layui-nav-child').css("display","");
        isShow = true;
    }
});

function loadchild(data) {
    var content="<dl class=\"layui-nav-child\">";
    $.each(data.children,function (i,note) {
        content+="<dd>";
        content+='<a href="javascript:;" lay-id="'+note.menuId+'" data-url="'+note.url+'" data-title="'+note.menuName+'"><i class="layui-icon '+note.icon+'" style="padding-left: 10px"></i><span  style=\'margin-left: 10px\'>'+note.menuName+'</span></a>'
        if (note.children ==null){
            return
        }
        content+=loadchild(note);
        content+="</dd>";
    })
    content+="</dl>";
    return content;
}