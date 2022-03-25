/*
 * Copyright (C)  guolin, PermissionX Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.easy.lib_util.permission.callback;

import androidx.annotation.NonNull;

import java.util.List;

/**
 *
 * @author guolin
 * @since 2020/6/7
 */
public interface RequestCallback {

    /**
     * 权限申请回调
     * @param allGranted 权限全部同意
     * @param grantedList 用户授予的权限
     * @param deniedList 用户拒绝的权限
     */
    void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList);

}
