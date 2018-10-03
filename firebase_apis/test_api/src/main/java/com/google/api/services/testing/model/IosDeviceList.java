/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * Modify at your own risk.
 */

package com.google.api.services.testing.model;

/**
 * A list of iOS device configurations in which the test is to be executed.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the Cloud Testing API. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class IosDeviceList extends com.google.api.client.json.GenericJson {

  /**
   * Required. A list of iOS devices
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<IosDevice> iosDevices;

  static {
    // hack to force ProGuard to consider IosDevice used, since otherwise it would be stripped out
    // see https://github.com/google/google-api-java-client/issues/543
    com.google.api.client.util.Data.nullOf(IosDevice.class);
  }

  /**
   * Required. A list of iOS devices
   * @return value or {@code null} for none
   */
  public java.util.List<IosDevice> getIosDevices() {
    return iosDevices;
  }

  /**
   * Required. A list of iOS devices
   * @param iosDevices iosDevices or {@code null} for none
   */
  public IosDeviceList setIosDevices(java.util.List<IosDevice> iosDevices) {
    this.iosDevices = iosDevices;
    return this;
  }

  @Override
  public IosDeviceList set(String fieldName, Object value) {
    return (IosDeviceList) super.set(fieldName, value);
  }

  @Override
  public IosDeviceList clone() {
    return (IosDeviceList) super.clone();
  }

}
