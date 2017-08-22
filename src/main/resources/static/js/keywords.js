/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Created by Piotr on 08.04.2017.
 */

$(document).ready(function () {

    function deleteKeyword(id) {
        var token = $("input[name='_csrf']").val();
        var header = "X-CSRF-TOKEN";
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
        $.ajax({
            url: '/admin/keywords/' + id + '/delete',
            type: 'DELETE',
            data: {CSRFToken: token, CSRF: header},
            success: function(xhr, status, error) {
                window.location = "/admin/keywords?success=true"
            },
            error: function(xhr, status, error) {
                window.location = "/admin/keywords?success=false"
            }
        });
    }

    $('#keyword-form-modal').on('show.bs.modal', function (event) {
        var keywordId = $(event.relatedTarget).data('keyword-id');

        if (keywordId) {
            $(this).find('.modal-title').text("Edycja słowa kluczowego");
            $(this).find('.button-submit').text("Aktualizuj");
            $("#id_form_div").show();

            var columns = $("#keyword-" + keywordId).find('td');
            $("#id_form").val(columns.eq(0).text());
            $("#id_form_static").text(columns.eq(0).text());
            $("#word_form").val(columns.eq(1).text());
        }
        else {
            $(this).find('.modal-title').text("Dodaj nowe słowo kluczowe");
            $(this).find('.button-submit').text("Dodaj");
            $("#id_form_div").hide();

            $("#id_form").val("0");
            $("#id_form_static").text("");
            $("#word_form").val("");
        }
    });

    $("#confirm-delete-modal").on('shown.bs.modal', function (event) {
        var keywordId = $(event.relatedTarget).data('keyword-id');

        var columns = $("#keyword-" + keywordId).find('td');
        $(this).find('.word-placeholder').text(columns.eq(1).text());
        $(this).find('.button-delete').data("keyword-id", keywordId);
    });

    $("#confirm-delete-modal").find('.button-delete').on("click", function (e) {
        deleteKeyword($(e.currentTarget).data("keyword-id"))
    });


});