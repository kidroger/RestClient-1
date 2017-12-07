package net.devfront.rest

class Pet {
    int id
    String type
    BigDecimal price


    @Override
    String toString() {
        return "Pet{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", price=" + price +
                '}'
    }
}
