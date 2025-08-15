/*
 * Copyright (c) 2025 Northview LLC.
 *
 * This file and its contents are confidential and proprietary to Northview LLC.
 * Unauthorized access, use, reproduction, modification, distribution, or disclosure of this
 * code, in whole or in part, is strictly prohibited without the express, prior, written, and
 * signed consent of Northview LLC or its authorized representatives.
 *
 * This code is private property and is NOT open source. If you are reading this notice
 * and do not have explicit permission to access this intellectual property, you are in
 * violation of Northview LLC's intellectual property rights and may be subject to civil
 * and/or criminal penalties under applicable law.
 */

package me.jrb326.simplePolls.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectLogger {}
