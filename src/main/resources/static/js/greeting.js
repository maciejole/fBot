/*
 *   Copyright 2012-2014 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/**
 * Created by Rafal Lebioda on 14.03.2017.
 */
var lastSelected = $(':focus');

jQuery(function ($) {
    $(".message-item").on('blur', function () {
        lastSelected = $(this);
    })

    $("#confirm-delete-modal").on('shown.bs.modal', function (event) {

        var greetingLocale = $(event.relatedTarget).data('greeting-locale');
        $(this).find('.button-delete').data("greeting-locale", greetingLocale)
    });

    $("#confirm-delete-modal").find('.button-delete').on("click", function (e) {
        removeGreeting($(e.currentTarget).data("greeting-locale"))

    })
});

function addText(el, text) {
    lastSelected.focus().val(lastSelected.val() + text);
    return false;
}


function removeGreeting(locale) {
    var token = $("input[name='_csrf']").val();
    var header = "X-CSRF-TOKEN";
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    $.ajax({
        url: '/admin/deleteGreeting/' + locale,
        type: 'DELETE',
        data: {CSRFToken: token, CSRF: header},
        success: function (xhr, status, error) {
            window.location = "/admin/greetings?success=true"
        },
        error: function (xhr, status, error) {
            window.location = "/admin/greetings?success=false"
        }
    });
}







