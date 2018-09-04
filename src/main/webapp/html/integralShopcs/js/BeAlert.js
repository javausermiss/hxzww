if (typeof $ === 'function') {
    $(function () {
        var BeAlert = {
            defaultConfig: {
                width: 250,
                height: 130,
                timer: 0,
                type: 'warning',
                showConfirmButton: true,
                showCancelButton: false,
                confirmButtonText: '确认',
                cancelButtonText: '取消'
            },
            html: '<div class="BeAlert_box">' +
            '<div class="BeAlert_title"></div>' +
            '<div class="BeAlert_message"></div>' +
            '<div class="BeAlert_button">' +
            '<button class="BeAlert_cancel"></button>' +
            '<button class="BeAlert_confirm"></button>' +
            '</div>' +
            '</div>',
            overlay: '<div class="BeAlert_overlay"></div>',
            open: function (title, message, callback, o) {
                var opts = {}, that = this;
                $.extend(opts, that.defaultConfig, o);
                $('body').append(that.html).append(that.overlay);
                var box = $('.BeAlert_box');
                box.css({
                    'width': opts.width + 'px',
                    // 'height': opts.height + 'px'
                });
                $('.BeAlert_image').addClass(opts.type);
                title && $('.BeAlert_title').html(title).show(),
                message && $('.BeAlert_message').html(message).show();
                var confirmBtn = $('.BeAlert_confirm'), cancelBtn = $('.BeAlert_cancel');
                opts.showConfirmButton && confirmBtn.text(opts.confirmButtonText).show(),
                opts.showCancelButton && cancelBtn.text(opts.cancelButtonText).show();
                $('.BeAlert_overlay').unbind('click').bind('click', function () {
                    that.close();
                });
                confirmBtn.unbind('click').bind('click', function () {
                    that.close();
                    typeof callback === 'function' && callback(true);
                });
                cancelBtn.unbind('click').bind('click', function () {
                    that.close();
                    typeof callback === 'function' && callback(false);
                });
            },
            close: function () {
                $(".BeAlert_overlay,.BeAlert_box").remove();
            }
        };
        window.bAlert = function (title, message, opts, callback) {

            if (typeof opts === 'function') {
                callback = opts;
                opts = BeAlert.defaultConfig;
            }

            BeAlert.open(title, message, callback, opts);
        };
        var _confirm = window.confirm;
        window.bConfirm = function (title, message, opts, callback) {
            if (typeof opts === 'function') {
                callback = opts;
                opts = BeAlert.defaultConfig;
            }
            opts = $.extend({type: 'question', showCancelButton: true}, opts);
            if (typeof callback === 'function') {
                BeAlert.open(title, message, callback, opts);
            } else {
                return _confirm(title);
            }
        }
    });
}



