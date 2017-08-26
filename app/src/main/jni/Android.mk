LOCAL_PATH := $(call my-dir)
LAME_PATH := libmp3lame

include $(CLEAR_VARS)
LOCAL_MODULE := WrapperLAME

LOCAL_SRC_FILES := \
            $(LAME_PATH)/bitstream.c \
          $(LAME_PATH)/encoder.c \
          $(LAME_PATH)/fft.c \
          $(LAME_PATH)/gain_analysis.c \
          $(LAME_PATH)/id3tag.c \
          $(LAME_PATH)/lame.c \
          $(LAME_PATH)/mpglib_interface.c \
          $(LAME_PATH)/newmdct.c \
          $(LAME_PATH)/presets.c \
          $(LAME_PATH)/psymodel.c \
          $(LAME_PATH)/quantize.c \
          $(LAME_PATH)/quantize_pvt.c \
          $(LAME_PATH)/reservoir.c \
          $(LAME_PATH)/set_get.c \
          $(LAME_PATH)/tables.c \
          $(LAME_PATH)/takehiro.c \
          $(LAME_PATH)/util.c \
          $(LAME_PATH)/vbrquantize.c \
          $(LAME_PATH)/VbrTag.c \
          $(LAME_PATH)/version.c \
        Wrapper.c

LOCAL_C_INCLUDES += $(LAME_PATH)

LOCAL_LDLIBS := -llog

LOCAL_CFLAGS = -O3 -DSTDC_HEADERS

include $(BUILD_SHARED_LIBRARY)