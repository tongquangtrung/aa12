#!/bin/bash

echo "🚀 Bắt đầu chạy app lên máy ảo..."

# Màu sắc cho output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Kiểm tra xem có emulator nào đang chạy không
echo "📱 Kiểm tra emulator đang chạy..."
RUNNING_DEVICES=$(adb devices | grep emulator | wc -l)

if [ "$RUNNING_DEVICES" -eq 0 ]; then
    echo -e "${YELLOW}⚠️  Không có emulator nào đang chạy${NC}"
    echo "📋 Danh sách AVD có sẵn:"
    emulator -list-avds

    echo ""
    echo -e "${YELLOW}Vui lòng chọn một trong các cách sau:${NC}"
    echo "1. Mở Android Studio > AVD Manager > Start emulator"
    echo "2. Hoặc chạy lệnh: emulator -avd <TÊN_AVD> &"
    echo ""
    echo "Sau đó chạy lại script này!"
    exit 1
fi

echo -e "${GREEN}✓ Tìm thấy emulator đang chạy${NC}"

# Đợi device sẵn sàng
echo "⏳ Đợi emulator sẵn sàng..."
adb wait-for-device
echo -e "${GREEN}✓ Emulator đã sẵn sàng${NC}"

# Build và cài đặt app
echo "🔨 Build và cài đặt app..."
./gradlew :app:installDebug

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Build thất bại!${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Cài đặt thành công${NC}"

# Launch app bằng cách mở từ launcher (monkey command)
echo "🚀 Khởi chạy app..."
adb shell monkey -p com.example.quanlydodung -c android.intent.category.LAUNCHER 1

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ App đã được khởi chạy thành công trên emulator!${NC}"
    echo ""
    echo -e "${YELLOW}Bạn có thể xem app trên emulator ngay bây giờ!${NC}"
    echo ""
    echo -e "${YELLOW}📊 Để xem logcat, chạy lệnh:${NC}"
    echo "adb logcat | grep -E 'AndroidRuntime|quanlydodung'"
else
    echo -e "${RED}❌ Không thể khởi chạy app${NC}"
    echo ""
    echo -e "${YELLOW}💡 Bạn có thể mở app thủ công:${NC}"
    echo "- Tìm icon app 'Quanlydodung' trên emulator"
    echo "- Hoặc chạy: adb shell am start -n com.example.quanlydodung/.CategoryGridActivity"
fi
