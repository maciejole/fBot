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
 * Created by Rafal Lebioda on 26.03.2017.
 */

$(document).ready(function() {

    function deleteProduct(id) {
        var token = $("input[name='_csrf']").val();
        var header = "X-CSRF-TOKEN";
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
        $.ajax({
            url: '/admin/products/' + id + '/delete',
            type: 'DELETE',
            success: function(result) {
                window.location = "/admin/products"
            },
            error: function (result) {
            }
        });
    }

    $('#product-form-modal').on('show.bs.modal', function (event) {
        var productId = $(event.relatedTarget).data('product-id');
        $("#keywords_form").tagsinput('removeAll');

        if (productId) {
            $(this).find('.modal-title').text("Edycja produktu");
            $(this).find('.button-submit').text("Aktualizuj");
            $("#id_form_div").show();

            $.ajax({
                url: "/admin/products/"+productId+"/keywords",
                success: function(data) {
                    $.each(data, function(index, item) {
                        $("#keywords_form").tagsinput('add', item);
                    })
                }
            });

            var columns = $("#product-"+productId).find('td');
            $("#id_form").val(columns.eq(0).text());
            $("#id_form_static").text(columns.eq(0).text());
            $("#name_form").val(columns.eq(1).text());
            $("#description_form").val(columns.eq(2).text());
            $("#imageUrl_form").val(columns.eq(4).find("img").attr('src'));
        } else {
            $(this).find('.modal-title').text("Dodaj nowy produkt");
            $(this).find('.button-submit').text("Dodaj");
            $("#id_form_div").hide();

            $("#id_form").val("0");
            $("#id_form_static").text("");
            $("#name_form").val("");
            $("#description_form").val("");
            $("#keywords_form").val("");
            $("#imageUrl_form").val("");
        }
    });

    $("#confirm-delete-modal").on('shown.bs.modal', function(event){
        var productId = $(event.relatedTarget).data('product-id');

        var columns = $("#product-"+productId).find('td');
        $(this).find('.name-placeholder').text(columns.eq(1).text());
        $(this).find('.button-delete').data("product-id",productId);
    });

    $("#confirm-delete-modal").find('.button-delete').on("click", function(e){deleteProduct($(e.currentTarget).data("product-id"))});

    var keywords = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.whitespace,
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        // prefetch: '/admin/products/get_all_keywords_suggestions',
        remote: {
            url: '/admin/product/keyword/suggestions?searchTerm=%QUERY',
            wildcard: '%QUERY'
        },
        limit: 6
    });
    keywords.initialize();

    $('.keywords').tagsinput({
        allowDuplicates: false,
        trimValue: true,
        typeaheadjs: {
            source: keywords.ttAdapter()
        }
    });

});