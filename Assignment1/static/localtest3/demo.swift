import Metal

let device = MTLCreateSystemDefaultDevice()!
let capacity = 4096

let pointer = UnsafeMutableRawPointer.allocate(byteCount: capacity, alignment: 0x1000)
print ("pointer addr: \(pointer.debugDescription)")

let buffer = device.makeBuffer(bytesNoCopy: pointer, length: capacity, options: [.storageModeShared]) { (pointer, capacity) in
    pointer.deallocate()
}!



