/**
 * Autogenerated by Thrift Compiler (0.21.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.example.CalendarManagement.generated;


@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.21.0)", date = "2025-02-18")
public enum MeetingStatus implements org.apache.thrift.TEnum {
  PENDING(0),
  ACCEPTED(1),
  DECLINED(2),
  CANCELLED(3);

  private final int value;

  private MeetingStatus(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  @Override
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  @org.apache.thrift.annotation.Nullable
  public static MeetingStatus findByValue(int value) { 
    switch (value) {
      case 0:
        return PENDING;
      case 1:
        return ACCEPTED;
      case 2:
        return DECLINED;
      case 3:
        return CANCELLED;
      default:
        return null;
    }
  }
}
