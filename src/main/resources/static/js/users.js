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

$(document).ready(function() {

    function deleteUser(id) {
        var token = $("input[name='_csrf']").val();
        var header = "X-CSRF-TOKEN";
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
        $.ajax({
            url: '/admin/users/' + id + '/delete',
            type: 'DELETE',
            data: {CSRFToken: token, CSRF: header},
            success: function(xhr, status, error) {
                window.location = "/admin/users?success=true";
            },
            error: function (xhr, status, error) {
                window.location = "/admin/users?success=false";
            }
        });
    }

    $("#confirm-delete-modal").on('shown.bs.modal', function(event){
        var userId = $(event.relatedTarget).data('user-id');

        var columns = $("#user-" + userId).find('td');
        $(this).find('.name-placeholder').text(columns.eq(1).text());
        $(this).find('.button-delete').data("user-id", userId);
    });

    $("#confirm-delete-modal").find('.button-delete').on("click", function(e){deleteUser($(e.currentTarget).data("user-id"))});

    $('#user-form-modal').on('show.bs.modal', function (event) {
        var userId = $(event.relatedTarget).data('user-id');

        if (userId) {
            $(this).find('.modal-title').text("Edycja użytkownika");
            $(this).find('.button-submit').text("Aktualizuj");
            $("#id_form_div").show();

            $.ajax({
                url: "/admin/users/"+userId+"/roles",
                success: function(data) {
                    $("#roles_form").val(data);
                }
            });

            var columns = $("#user-" + userId).find('td');
            $("#id_form").val(columns.eq(0).text());
            $("#id_form_static").text(columns.eq(0).text());
            $("#email_form").val(columns.eq(1).text());
            $("#firstname_form").val(columns.eq(2).text());
            $("#lastname_form").val(columns.eq(3).text());
            $("#phoneNumber_form").val(columns.eq(4).text());
        } else {
            $(this).find('.modal-title').text("Dodaj nowego użytkownika");
            $(this).find('.button-submit').text("Dodaj");
            $("#id_form_div").hide();

            $("#id_form").val("0");
            $("#id_form_static").text("");
            $("#email_form").val("");
            $("#description_form").val("");
            $("#firstname_form").val("");
            $("#lastname_form").val("");
            $("#phoneNumber_form").val("");
        }
    });
});