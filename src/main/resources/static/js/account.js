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
 * Created by rafal on 25.05.2017.
 */

function editButtonOnClick() {
    $("#emailInput").prop('disabled', false);
    $("#nameInput").prop('disabled', false);
    $("#surnameInput").prop('disabled', false);
    $("#imageInput").prop('disabled', false);
    $("#phoneInput").prop('disabled', false);
    $("#button-edit").hide();
    $("#button-save").show();

}