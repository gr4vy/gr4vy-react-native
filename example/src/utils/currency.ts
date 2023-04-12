const intl = new Intl.NumberFormat('en-US')

export const formatCurrency = (value: number) => intl.format(value / 100)
